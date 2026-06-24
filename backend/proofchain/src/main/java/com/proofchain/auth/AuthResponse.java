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
 * - message: mensagem de retorno (sucesso ou erro)
 * - success: indica se o login foi bem-sucedido
 *
 * Evolução futura:
 * Na fase de JWT, este DTO poderá incluir o token de acesso.
 *
 * Fluxo:
 * 1. AuthService valida credenciais
 * 2. Resultado é encapsulado neste record
 * 3. AuthController retorna ao cliente
 *
 * Integração no sistema:
 * Usado como resposta padrão do endpoint de login.
 */
public record AuthResponse(
        String message,
        boolean success
) {
}
