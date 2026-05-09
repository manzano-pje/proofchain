package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.exception.InstitutionNotAutorizedException;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.plataform.domain.ModelMapperConfig;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.applications.command.CreateUserCommand;
import com.proofchain.user.domain.exception.UserRegisteredException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.time.Instant.now;

@Component
@AllArgsConstructor
public class CreateUserHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapperConfig mapper;
    private final InstitutionRepository institutionRepository;


    public UserReturn createUser(CreateUserCommand command) {
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        if (institutionId == null){
            throw new InstitutionNotAutorizedException();
        }
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(InstitutionNotFoundException::new);

        // Valida se usuário já existe
        Optional<User> userOptional = userRepository.findByNameAndInstitutionId(command.getName(), institutionId) ;
        if(userOptional.isPresent()){
            throw new UserRegisteredException();
        }

        // Cria usuário
        User user = User.create(
                command.getName(),
                command.getEmail(),
                passwordEncoder.encode(command.getPassword()),
                command.getRole(),
                institution
        );
        UserReturn response = new UserReturn(user);
        user = userRepository.save(user);
        return response;
    }
}
