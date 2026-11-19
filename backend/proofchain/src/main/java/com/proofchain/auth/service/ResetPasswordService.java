package com.proofchain.auth.service;

import com.proofchain.auth.dtos.NewPasswordDto;
import com.proofchain.shared.exception.InvalidTokenExceptions;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.PasswordExceptions;
import com.proofchain.shared.exception.messages.PasswordMessages;
import com.proofchain.shared.exception.messages.TokenMessages;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.util.EmailService;
import com.proofchain.user.applications.command.TokenValidationCommand;
import com.proofchain.auth.Entity.PasswordResetToken;
import com.proofchain.user.domain.model.User;
import com.proofchain.auth.repository.PasswordResetTokenRepository;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResetPasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetRepository;
    private final EmailService emailService;

    public void resetPassword(String email) {

        Optional<User> userOptional = userRepository.findByEmail(email);
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
        password.setUser(userOptional.get());
        password.setPassword(null);
        password.setToken(passwordEncoder.encode(token));
        password.setExpirationDate(Instant.now().plus(10, ChronoUnit.MINUTES));

        passwordResetRepository.save(password);

        /*
        ======================================
        ENVIAR E-MAIL COM TOKEN PARA O USUÁRIO
        =======================================
         */
        emailService.sendEmail(email, token);

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
        if(!passwordEncoder.matches(command.getToken(), passwordResetTokenOptional.get().getToken())){
            throw new InvalidTokenExceptions(TokenMessages.INVALID_TOKEN);
        }
        if(passwordResetTokenOptional.get().getExpirationDate().isBefore(Instant.now())){
            throw new InvalidTokenExceptions(TokenMessages.TOKEN_EXPIRED);
        }
    }

    public void newPassword(NewPasswordDto newPassword) {
        String password = passwordEncoder.encode(newPassword.password());
        Optional<User> userOptional = userRepository.findByEmail(newPassword.email());
        if(userOptional.isEmpty()) {
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
        }
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetRepository.findByUser_Id(userOptional.get().getId());
        if(passwordResetTokenOptional.isEmpty()) {
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
        }
        if(!passwordEncoder.matches(newPassword.token(), passwordResetTokenOptional.get().getToken())){
            throw new InvalidTokenExceptions(TokenMessages.INVALID_TOKEN);
        }
        if(passwordResetTokenOptional.get().getExpirationDate().isBefore(Instant.now())){
            throw new InvalidTokenExceptions(TokenMessages.TOKEN_EXPIRED);
        }
        if(passwordEncoder.matches(password, userOptional.get().getPassword())){
            throw new PasswordExceptions(PasswordMessages.PASSWORD_ALREADY_EXIST);
        }

        User user = passwordResetTokenOptional.get().getUser();
        user.setPassword(password);

        userRepository.save(user);
        passwordResetRepository.deleteByUser_id(user.getId());
    }
}
