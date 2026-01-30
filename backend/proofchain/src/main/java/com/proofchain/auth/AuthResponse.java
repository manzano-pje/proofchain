package com.proofchain.auth;

// DTO resposta do login

public class AuthResponse {

    private String token;

    public AuthResponse(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

}

/*
🔧 Versão mais otimizada (sênior):
- Retornar também:
  - expiresAt
  - role
  - institutionId
Motivo: frontend não precisa decodificar JWT
*/