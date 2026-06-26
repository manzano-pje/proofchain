package com.proofchain.shared.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig
 *
 * Responsabilidade:
 * Configuração central da segurança HTTP da API do ProofChain.
 *
 * Função no sistema:
 * Define quais endpoints são públicos ou protegidos, estabelece a política
 * de sessão como STATELESS e registra o filtro interceptador do JWT.
 *
 * Fluxo:
 * 1. A requisição HTTP chega ao servidor.
 * 2. Passa pelas regras configuradas no HttpSecurity.
 * 3. O JwtAuthenticationFilter intercepta e valida o token (se houver).
 * 4. O Spring Security autoriza ou barra o acesso com base nas permissões.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite o uso de @PreAuthorize nos Controllers para controle fino
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF (Cross-Site Request Forgery) pois o sistema é Stateless baseado em tokens
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configura as regras de autorização de requisições (Endpoints)
                .authorizeHttpRequests(auth -> auth
                        // Endpoint de autenticação (Login) explicitamente público
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Exemplo de liberação para documentação da API (Swagger/OpenAPI), se houver futuramente
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Qualquer outra requisição dentro da API exige autenticação obrigatória
                        .anyRequest().authenticated()
                )

                // 3. Define a política de sessão como estritamente STATELESS (sem estado no servidor)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Associa o provedor de autenticação customizado (configurado no SecurityBeansConfig)
                .authenticationProvider(authenticationProvider)

                // 5. Injeta o nosso filtro JWT antes do filtro padrão de autenticação por usuário/senha
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}