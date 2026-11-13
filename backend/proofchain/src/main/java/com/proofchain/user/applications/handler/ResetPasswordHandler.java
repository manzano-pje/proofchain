package com.proofchain.user.applications.handler;

import com.proofchain.shared.exception.InvalidTokenExceptions;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.TokenMessages;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.util.EmailService;
import com.proofchain.user.applications.command.ResetPasswordCommand;
import com.proofchain.user.applications.command.TokenValidationCommand;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.PasswordResetToken;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.PasswordResetTokenRepository;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ResetPasswordHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetRepository;
    private final EmailService emailService;

    public void resetPassword(ResetPasswordCommand command) {

        Optional<User> userOptional = userRepository.findByEmail(command.getEmail());
        if(userOptional.isEmpty()) {
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
        }

        /*
        ==================================
        DELETAR TOKEN ANTERIOR SE EXISTIR
        ==================================
         */
        passwordResetRepository.deleteByUser_id(userOptional.get().getId());

        /*
        ==================================
        GERAR TOKEN ALEATÓRIO DE 6 DÍGITOS
        ==================================
         */
        String token = String.format("%06d", new SecureRandom().nextInt(999999));

        /*
        ===============================================================================
        GRAVAR TOKEN E SENHA CRIPTOGRAFADA NO BANCO DE DADOS COM VALIDADE DE 10 MINUTOS
        ===============================================================================
         */
        PasswordResetToken password = new PasswordResetToken();
        password.setPassword(passwordEncoder.encode(command.getPassword()));
        password.setToken(token);
        password.setExpirationDate(Instant.now().plus(10, ChronoUnit.MINUTES));

        passwordResetRepository.save(password);

        /*
        ======================================
        ENVIAR E-MAIL COM TOKEN PARA O USUÁRIO
        =======================================
         */
        emailService.sendEmail(command.getEmail(), token);

    }

   /*
   ======================================
    VALIDAR CÓDIGO ENVIADO
   =======================================
    */

    // TODO: implementar quantidade de tentativas de validação do token
    public void tokenValidation(TokenValidationCommand command){
        Optional<User> userOptional = userRepository.findByEmail(command.getEmail());
        if(userOptional.isEmpty()) {
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
        }
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetRepository.findByUser_Id(userOptional.get().getId());
        if(passwordResetTokenOptional.isEmpty()) {
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
        }
        if(!passwordResetTokenOptional.get().getToken().equals(command.getToken())){
            throw new InvalidTokenExceptions(TokenMessages.INVALID_TOKEN);
        }
        if(passwordResetTokenOptional.get().getExpirationDate().isBefore(Instant.now())){
            throw new InvalidTokenExceptions(TokenMessages.TOKEN_EXPIRED);
        }

        User user = passwordResetTokenOptional.get().getUser();
        user.setPassword(passwordResetTokenOptional.get().getPassword());

        userRepository.save(user);
        passwordResetRepository.deleteByUser_id(user.getId());
    }
}
