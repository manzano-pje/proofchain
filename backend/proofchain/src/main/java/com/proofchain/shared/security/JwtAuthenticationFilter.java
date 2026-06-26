package com.proofchain.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JwtAuthenticationFilter
 *
 * Filtro responsável por interceptar requisições HTTP e realizar autenticação
 * baseada em JWT dentro do contexto do Spring Security.
 *
 * Este filtro é executado uma única vez por requisição e garante que o token
 * seja validado antes do acesso aos recursos protegidos da aplicação.
 *
 * Fluxo de funcionamento:
 *
 * - Extrai o token do header Authorization
 * - Valida o formato Bearer
 * - Extrai o username (subject) do token
 * - Carrega o usuário via UserDetailsService
 * - Valida o token com JwtService
 * - Cria autenticação no SecurityContext
 * - Extrai dados de tenant (institutionId e role)
 * - Define contexto multi-tenant para a requisição
 * - Libera a requisição para os próximos filtros
 * - Limpa o contexto de tenant ao final da execução
 *
 * Responsabilidades:
 *
 * - Autenticar requisições via JWT
 * - Integrar Spring Security com JwtService
 * - Garantir isolamento multi-tenant por requisição
 *
 * Não é responsabilidade desta classe:
 *
 * - Gerar tokens JWT
 * - Persistir dados
 * - Executar regras de negócio
 * - Gerenciar autorização de domínio
 *
 * Integrações:
 *
 * - JwtService (validação e parsing do token)
 * - UserDetailsService (carregamento do usuário)
 * - SecurityConfig (configuração da security chain)
 * - TenantContext (isolamento multi-tenant)
 *
 * Observação:
 *
 * Este filtro atua exclusivamente na camada de segurança.
 * Toda regra de negócio deve ser tratada nas camadas superiores.
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {

            // 1. extrai username do token
            final String username = jwtService.extractUsername(token);

            // 2. verifica se já não está autenticado
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 3. carrega usuário real do banco
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 4. valida token contra usuário
                if (jwtService.validateToken(token, userDetails)) {

                    // 5. cria autenticação
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

                    // 6. tenant context (SÓ após autenticação válida)
                    Long institutionId = jwtService.extractInstitutionId(token);
                    String role = jwtService.extractRole(token);

                    if ("SUPER_ADMIN".equals(role)) {
                        TenantContext.setInstitutionId(1L);
                    } else if (institutionId != null) {
                        TenantContext.setInstitutionId(institutionId);
                    }
                }
            }

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }
}