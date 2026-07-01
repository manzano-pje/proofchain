package com.proofchain.auth;

import com.proofchain.shared.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

/**
 * AuthService
 *
 * Responsabilidade:
 * Orquestra o processo de autenticação dos usuários da aplicação.
 *
 * Função no sistema:
 * Recebe as credenciais informadas pelo usuário, delega a autenticação ao
 * AuthenticationManager e, após a validação bem-sucedida, solicita ao
 * JwtService a geração do token de acesso (JWT).
 *
 * Fluxo de utilização:
 * 1. Recebe as credenciais enviadas pelo AuthController.
 * 2. Solicita a autenticação ao AuthenticationManager.
 * 3. Recupera o usuário autenticado.
 * 4. Gera o JWT através do JwtService.
 * 5. Retorna o AuthResponse ao controlador.
 *
 * Integração no sistema:
 * Atua entre o AuthController e a infraestrutura de autenticação do Spring
 * Security, centralizando todo o fluxo de login da aplicação.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * Responsável por autenticar as credenciais do usuário.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Responsável pela geração e validação dos tokens JWT.
     */
    private final JwtService jwtService;

}