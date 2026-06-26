package com.proofchain.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JwtAuthenticationFilter
 *
 * Responsabilidade:
 * Intercepta requisições HTTP protegidas para realizar a autenticação baseada
 * em JSON Web Token (JWT).
 *
 * Função no sistema:
 * Valida o token recebido, recupera o usuário autenticado, registra a
 * autenticação no Spring Security e configura o contexto de tenant para
 * isolamento multi-tenant.
 *
 * Fluxo de utilização:
 * 1. Extrai o token do cabeçalho Authorization.
 * 2. Valida o JWT utilizando o JwtService.
 * 3. Recupera o usuário através do UserDetailsService.
 * 4. Registra a autenticação no SecurityContextHolder.
 * 5. Configura o TenantContext da requisição.
 * 6. Continua a cadeia de filtros.
 *
 * Integração no sistema:
 * Atua entre o SecurityConfig e os recursos protegidos da aplicação,
 * utilizando o JwtService para manipulação do token e o UserDetailsService
 * para recuperação do usuário autenticado.
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
        ) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(BEARER_PREFIX_LENGTH );

        try {

            // Extrai username do token
            final String username = jwtService.extractUsername(token);

            // Verifica se já não está autenticado
            UserDetails userDetails;
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Carrega usuário real do banco
                userDetails = userDetailsService.loadUserByUsername(username);

                // Valida token contra usuário
                if (jwtService.validateToken(token, userDetails)) {
                    authenticate(userDetails, request);
                    configurationTenat(token);
                }
            }
            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Configura o contexto de tenant da requisição atual.
     *
     * @param token JWT da requisição autenticada
     */
    private void configurationTenat(String token){
        // Tenant context (SÓ após autenticação válida)
        Long institutionId = jwtService.extractInstitutionId(token);
        String role = jwtService.extractRole(token);

        if ("SUPER_ADMIN".equals(role)) {
            TenantContext.setInstitutionId(1L);
        } else if (institutionId != null) {
            TenantContext.setInstitutionId(institutionId);
        }
    }

    /**
     * Registra o usuário autenticado no contexto de segurança do Spring.
     *
     * @param userDetails usuário autenticado
     * @param request requisição HTTP atual
     */
    private void authenticate(UserDetails userDetails, HttpServletRequest request)
    {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

    }
}