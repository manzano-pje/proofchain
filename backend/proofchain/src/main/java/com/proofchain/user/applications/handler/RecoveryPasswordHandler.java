package com.proofchain.user.applications.handler;

import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.applications.command.RecoveryPasswordCommand;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class RecoveryPasswordHandler {

    private final UserRepository userRepository;
    private final TenantValidation tenantValidation;
    private final PasswordEncoder passwordEncoder;

    public void recoveryPassword(RecoveryPasswordCommand command) {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);

        /**
         * Localiza um usuário pelo e-mail pertencente a uma instituição.
         *
         * @param email e-mail do usuário
         * @param id identificador da instituição
         * @return usuário encontrado, caso exista
         */
        Optional<User> userOptional = userRepository.findByEmail(command.getEmail());
        if(userOptional.isEmpty()) {
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(command.getPassword()));

        userRepository.save(user);
    }
}
