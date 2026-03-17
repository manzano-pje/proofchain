package com.proofchain.Dtos.response;

// DTO resposta do login

public class AuthResponseDto {

    private String token;

    public AuthResponseDto(String token){this.token = token;}
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