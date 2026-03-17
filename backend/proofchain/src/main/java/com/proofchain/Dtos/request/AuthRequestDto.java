package com.proofchain.Dtos.request;

// Dados do Login

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthRequestDto {

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
- Usar record: AuthRequestDto(String email, String password)
Motivo: menos código e validação automática
*/