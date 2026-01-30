package com.proofchain.auth;

// Dados do Login

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class AuthRequest {

    @Email
    @NotBlank
    // e-mail de login
    private String email;

    @NotBlank
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }



}

/*
🔧 Versão mais otimizada (sênior):
- Usar record: AuthRequest(String email, String password)
Motivo: menos código e validação automática
*/