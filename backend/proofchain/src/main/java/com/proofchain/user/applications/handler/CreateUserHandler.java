package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.model.Institution;
import com.proofchain.plataform.domain.ModelMapperConfig;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.request.UserRequestDto;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static java.time.Instant.now;

@Component
@AllArgsConstructor
public class CreateUserHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapperConfig mapper;
    private final Validations validations;

    public UserReturn createUser(UserRequestDto newUser) {
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = validations.validateinstitution(institutionId);

        // Valida se usuário já existe
        validations.validateUserExist(newUser.getEmail(), institutionId);

        // Cria usuário
        User user = new User();
        user = mapper.modelMapper().map(newUser, User.class);
        user.setInstitution(institution);
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setCreateAt(now());
        user.setActive(true);
        user = userRepository.save(user);

        return mapper.modelMapper().map(user, UserReturn.class);
    }
}
