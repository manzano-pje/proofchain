package com.proofchain.auth;

/**
 * AuthRequest
 *
 * Função no sistema:
 * Representa o payload de autenticação enviado pelo cliente no processo de login.
 * Transporta as credenciais necessárias para validação do usuário no sistema.
 *
 * Estrutura atual:
 * Record imutável contendo username e password.
 * Utilizado como DTO de entrada na camada de autenticação.
 *
 * Fluxo:
 * 1. Cliente envia requisição de login
 * 2. Spring mapeia o JSON para AuthRequest
 * 3. AuthService consome os dados para autenticação
 *
 * Integração no sistema:
 * Utilizado pelo AuthController como entrada do endpoint /auth/login.
 */
public record AuthRequest(
        String username,
        String password
) {
}