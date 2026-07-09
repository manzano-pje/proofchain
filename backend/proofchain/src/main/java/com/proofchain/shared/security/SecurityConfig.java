package com.proofchain.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig
 *
 * Função no sistema:
 * Centraliza a configuração de segurança da aplicação ProofChain, definindo políticas de autenticação,
 * autorização, gerenciamento de sessão e integração do filtro JWT.
 *
 * Estrutura atual:
 * Configuração baseada em Spring Security com autenticação stateless via JWT.
 * Define regras de acesso HTTP e integra filtros personalizados de autenticação.
 *
 * Fluxo:
 * 1. Requisições HTTP entram na aplicação
 * 2. SecurityFilterChain aplica regras de segurança
 * 3. JwtAuthenticationFilter intercepta requisições protegidas
 * 4. JwtAuthenticationEntryPoint trata falhas de autenticação
 * 5. AuthenticationManager é utilizado no fluxo de login
 *
 * Integração no sistema:
 * Base da camada de segurança do ProofChain, responsável por proteger todos os endpoints
 * exceto rotas públicas de autenticação.
 */
@Configuration
public class SecurityConfig {

    /*
     * =========================================================
     * DEPENDÊNCIAS DE SEGURANÇA
     * =========================================================
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Nota de decisão:
     * A injeção via construtor garante imutabilidade das dependências de segurança
     * e facilita testes da configuração de segurança.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    /*
     * =========================================================
     * CONFIGURAÇÃO DO SECURITY FILTER CHAIN
     * =========================================================
     */

    /**
     * Define a cadeia principal de filtros de segurança da aplicação.
     *
     * @param http configuração HTTP do Spring Security
     * @return SecurityFilterChain configurado
     * @throws Exception erro de configuração de segurança
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/api/v1/institution"
                        ).permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}