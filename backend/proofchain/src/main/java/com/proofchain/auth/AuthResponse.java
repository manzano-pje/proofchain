package com.proofchain.auth;

/**
 * AuthResponse
 *
 * Função no sistema:
 * Representa a resposta do processo de autenticação do usuário, encapsulando o token JWT gerado após validação das credenciais.
 *
 * Estrutura atual:
 * Record imutável contendo:
 * - token: JWT de acesso à plataforma contendo claims de autenticação e autorização:
 *   - sub: identificador do usuário (username/email)
 *   - userId: identificador interno do usuário
 *   - institutionId: identificador do tenant (instituição)
 *   - role: autoridade do usuário (ex: SUPER_ADMIN, ADMIN, USER)
 *   - iat: timestamp de emissão do token
 *   - exp: timestamp de expiração do token
 *
 * Fluxo:
 * 1. AuthService autentica o usuário via AuthenticationManager
 * 2. JwtService gera o token JWT com claims estruturadas
 * 3. AuthResponse encapsula o token gerado
 * 4. AuthController retorna a resposta ao cliente
 *
 * Integração no sistema:
 * Utilizado como payload de resposta do endpoint /auth/login, servindo como contrato de autenticação do sistema.
 */
public record AuthResponse(
        String token
) {
}