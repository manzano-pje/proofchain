package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.model.Institution;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class DeleteUserHandler {

    private final UserRepository userRepository;
    private final Validations validations;

    public void deleteUSer(String email){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = validations.validateinstitution(institutionId);

        Optional<User> userOptional = validations.validateUserNotExist(email, institutionId);
        userRepository.deleteByEmail(email);
    }
}
