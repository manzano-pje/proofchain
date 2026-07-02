package com.proofchain.shared.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAuthenticationEntryPoint
 *
 * Função no sistema:
 * Responsável por interceptar requisições não autenticadas e padronizar a resposta HTTP 401 (Unauthorized)
 * dentro do fluxo de segurança do sistema ProofChain.
 *
 * Estrutura atual:
 * Componente Spring Security que implementa AuthenticationEntryPoint.
 * Atua como ponto central de tratamento de falhas de autenticação.
 *
 * Fluxo:
 * 1. Requisição protegida é realizada sem token válido ou sem autenticação
 * 2. Spring Security delega o controle de resposta para este entry point
 * 3. Resposta HTTP 401 (Unauthorized) é retornada ao cliente
 *
 * Integração no sistema:
 * Integrado à SecurityConfig, garantindo padronização global de respostas de autenticação inválida.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}