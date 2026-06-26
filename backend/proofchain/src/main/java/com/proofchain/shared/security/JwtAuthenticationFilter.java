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
 * Filtro de segurança responsável por interceptar todas as requisições HTTP
 * feitas à API do ProofChain, validando o token JWT presente no cabeçalho.
 * * Este componente atua na camada de infraestrutura e segurança, garantindo
 * que apenas requisições autenticadas e íntegras cheguem aos endpoints protegidos.
 * * Responsabilidades:
 * - Interceptar requisições HTTP uma única vez por ciclo de vida (OncePerRequestFilter);
 * - Extrair o token Bearer do cabeçalho 'Authorization';
 * - Validar a autenticidade, assinatura e expiração do token via JwtService;
 * - Isolar o contexto Multi-Tenant definindo o 'institutionId' no ThreadLocal;
 * - Configurar o Contexto de Segurança do Spring (SecurityContextHolder).
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

        // 1. Extrai o cabeçalho de Autorização
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Valida se o cabeçalho existe e começa com o prefixo "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai apenas o hash do token (removendo os 7 caracteres de "Bearer ")
        jwt = authHeader.substring(7);

        // Extrai o e-mail (subject) de dentro do token
        userEmail = jwtService.extractUsername(jwt);

        // 2. Verifica se o e-mail existe e se o usuário já não está autenticado no contexto atual
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Busca os detalhes do usuário no banco de dados
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 3. Valida se o token é estritamente válido e pertence a este usuário
            if (jwtService.validateToken(jwt, userDetails)) {

                // Cria o objeto de autenticação padrão do Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Vincula os detalhes da requisição HTTP (IP, Sessão) ao token de autenticação
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Injeta o usuário autenticado no contexto global de segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);

                try {
                    // 4. PASSO MULTI-TENANT: Extrai o ID da instituição e injeta no ThreadLocal
                    Long institutionId = jwtService.extractClaim(jwt, claims -> claims.get("institutionId", Long.class));
                    String role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));

                    // Regra de negócio: Se for SUPER_ADMIN, ignora o isolamento ou define como 0
                    if ("SUPER_ADMIN".equals(role)) {
                        TenantContext.setInstitutionId(1L);
                    } else if (institutionId != null) {
                        TenantContext.setInstitutionId(institutionId);
                    }

                    // Permite que a requisição continue para o próximo filtro/controller
                    filterChain.doFilter(request, response);

                } finally {
                    // 5. LIMPEZA DE MEMÓRIA: No bloco finally, garante que o contexto do Tenant
                    // seja limpo para evitar Memory Leaks entre threads de usuários diferentes.
                    TenantContext.clear();
                }

                return;
            }
        }

        // Se o token for inválido ou não passar nas verificações, apenas continua a cadeia (gerará 403/401)
        filterChain.doFilter(request, response);
    }
}