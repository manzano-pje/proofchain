package com.proofchain.shared.exception.messages;

import com.proofchain.shared.exception.UnauthorizedException;
import org.springframework.security.authentication.*;

public class AuthMessages {

    private AuthMessages() {}

    public static final String INVALID_CREDENTIALS = "Usuário ou senha inválidos.";
    public static final String ACCESS_DENIED = "Acesso negado.";
    public static final String FORBIDDEN = "Acesso negado.";
    public static final String ACCOUNT_EXPIRED = "Sua conta está expiradas.";
    public static final String ACCOUNT_DISABLED = "Sua conta está inativa.";
    public static final String CREDENTIALS_EXPIRED = "Suas credenciais expiraram.";
}

