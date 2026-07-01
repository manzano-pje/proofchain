package com.proofchain.auth;

/**
 * AuthResponse
 *
 * Responsabilidade:
 * Representar a resposta do processo de login.
 *
 * Função no sistema:
 * Retorna o resultado da autenticação do usuário após validação das credenciais.
 *
 * Estrutura atual:
 * - token: token de acesso para a plataforma contendo:
 *           - sub: e-mail de login do usuário
 *           - userId: id do usuário,
 *           - institutionId: id da instituição do usuário
 *           - role: autoridade do usuário (SUPER_ADMIN, ADMIN, USER)
 *           - iat: horário de geração do token,
 *           - exp:  horário em que expira o token

 * Fluxo:
 * 1. AuthService valida credenciais
 * 2. Resultado é encapsulado neste record
 * 3. AuthController retorna ao cliente
 *
 * Integração no sistema:
 * Usado como resposta padrão do endpoint de login.
 */
public record AuthResponse(
        String token
) {
}
