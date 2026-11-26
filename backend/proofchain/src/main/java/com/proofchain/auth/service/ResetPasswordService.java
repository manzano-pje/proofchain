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
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordService {

    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;
    private  PasswordResetTokenRepository passwordResetRepository;
    private  EmailService emailService;


    @Transactional
    public void resetPassword(String email) {

        // 1. Busca o usuário pelo e-mail informado.
        // Se não existir no banco, lança NotFoundException e interrompe o método.
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
        }

        // Guarda o usuário encontrado em uma variável para evitar chamar userOptional.get() várias vezes
        User user = userOptional.get();

    /*
    ==================================
    DELETAR TOKEN ANTERIOR SE EXISTIR
    ==================================
     */
        // ATENÇÃO: aqui buscamos pelo ID do usuário (e não pelo ID do registro de token),
        // pois um usuário pode ter ou não um token de reset já gerado anteriormente.
        // (Ajuste o nome do método conforme o seu PasswordResetTokenRepository, ex: findByUserId / deleteByUserId)
        PasswordResetToken resetToken = passwordResetRepository.findByUser_Id(user.getId())
                .orElseGet(PasswordResetToken::new);

    /*
    ==================================
    GERAR TOKEN ALEATÓRIO DE 6 DÍGITOS
    ==================================
     */
        // SecureRandom é mais seguro que Random comum para códigos/tokens.
        // nextInt(1_000_000) gera números de 0 até 999999, e "%06d" preenche com zeros à esquerda (ex: "004812").
        String token = String.format("%06d", new SecureRandom().nextInt(1_000_000));

    /*
    ===============================================================================
    GRAVAR TOKEN E SENHA CRIPTOGRAFADA NO BANCO DE DADOS COM VALIDADE DE 10 MINUTOS
    ===============================================================================
     */
        resetToken.setUser(user);
        resetToken.setPassword(null);
        resetToken.setToken(passwordEncoder.encode(token));
        resetToken.setExpirationDate(Instant.now().plus(10, ChronoUnit.MINUTES));

        passwordResetRepository.save(resetToken);

    /*
    ======================================
    ENVIAR E-MAIL COM TOKEN PARA O USUÁRIO
    =======================================
     */
        // Envia o código puro (6 dígitos) por e-mail para o usuário validar na tela de redefinição
        emailService.sendEmail(user.getEmail(), token);
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
        passwordResetRepository.deleteByUser_Id(user.getId());
    }
}
