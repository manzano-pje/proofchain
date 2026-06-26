package com.proofchain.auth;

/**
 * AuthRequest
 *
 * Responsabilidade:
 * Representar os dados enviados pelo cliente no processo de login.
 *
 * Função no sistema:
 * Transporta as credenciais necessárias para validação do usuário.
 *
 * Fluxo:
 * 1. Cliente envia requisição de login
 * 2. Dados são mapeados para este record
 * 3. AuthService utiliza as credenciais para validação
 *
 * Integração no sistema:
 * Utilizado pelo AuthController no endpoint de login.
 */
public record AuthRequest(
        String username,
        String password
) {
}
