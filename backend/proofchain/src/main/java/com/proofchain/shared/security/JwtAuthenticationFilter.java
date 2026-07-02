package com.proofchain.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter
 *
 * Função no sistema:
 * Responsável por interceptar requisições HTTP e aplicar autenticação baseada em JWT.
 * Extrai o token do header Authorization, valida sua autenticidade e popula o SecurityContext
 * com o usuário autenticado.
 *
 * Estrutura atual:
 * Filtro de segurança baseado em Spring Security (OncePerRequestFilter).
 * Atua na camada de segurança da aplicação, integrando JwtService e UserDetailsServiceImpl.
 *
 * Fluxo:
 * 1. Intercepta requisição HTTP
 * 2. Extrai header Authorization
 * 3. Valida prefixo "Bearer"
 * 4. Extrai token JWT
 * 5. Obtém username via JwtService
 * 6. Carrega UserDetails via UserDetailsServiceImpl
 * 7. Valida token
 * 8. Cria Authentication object
 * 9. Popula SecurityContextHolder
 * 10. Continua cadeia de filtros
 *
 * Integração no sistema:
 * Integrado à SecurityConfig como filtro principal de autenticação do sistema ProofChain.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Nota de decisão:
     * A injeção via construtor foi escolhida para garantir imutabilidade das dependências
     * e facilitar testes unitários do filtro.
     */
    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /*
     * =========================================================
     * PROCESSAMENTO DO FILTRO
     * =========================================================
     */

    /**
     * Intercepta a requisição HTTP e aplica validação JWT antes da execução do fluxo.
     *
     * @param request requisição HTTP recebida
     * @param response resposta HTTP
     * @param filterChain cadeia de filtros do Spring Security
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        final String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {

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

        filterChain.doFilter(request, response);
    }
}

