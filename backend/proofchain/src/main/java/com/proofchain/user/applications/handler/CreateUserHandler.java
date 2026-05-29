package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.exception.InstitutionNotAutorizedException;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.plataform.domain.ModelMapperConfig;
import com.proofchain.user.applications.command.CreateUserCommand;
import com.proofchain.user.domain.exception.UserRegisteredException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
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


    public void createUser(CreateUserCommand command) {
        // 🔑 Instituição vem do TOKEN, não do request
        //      Long institutionId = SecurityUtils.getInstitutionId();

        Long institutionId = 1L;
        if (institutionId == null){
            throw new InstitutionNotAutorizedException();
        }
        Optional<Institution> optionalInstitution = institutionRepository
                .findByIdAndDeletedAtIsNull(institutionId);
        if(optionalInstitution.isEmpty()) {
                throw new InstitutionNotFoundException();
        }

        // Valida se usuário já existe
        boolean existUSer = userRepository.existsByNameAndInstitutionId(command.getName(), institutionId) ;
        if(existUSer){
            throw new UserRegisteredException();
        }

        // Cria usuário
        User user = User.create(
                command.getName(),
                command.getEmail(),
                passwordEncoder.encode(command.getPassword()),
                command.getRole(),
                optionalInstitution.get()
        );
        userRepository.save(user);
    }
}
