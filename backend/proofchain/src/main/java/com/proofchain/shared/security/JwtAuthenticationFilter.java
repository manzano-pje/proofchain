package com.proofchain.shared.security;


// Leitura do token
// Filtro executado em todas as requisições

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {

        String path = request.getServletPath();

        // 🔕 Ignora Swagger e OpenAPI
        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")) {

            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(("Authorization"));

        if (header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);
            Claims claims = jwtService.validateToken(token);

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            // 🔥 NÃO BLOQUEIA
            System.out.println("Erro ao validar token: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}

/*
🔧 Versão mais otimizada (sênior):
- Authentication customizado
- Extrair tenant para contexto
Motivo: multi-tenant automático
*/