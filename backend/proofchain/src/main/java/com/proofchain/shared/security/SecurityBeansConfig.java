package com.proofchain.shared.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SecurityBeansConfig
 *
 * Função no sistema:
 * Centraliza a configuração e exposição dos beans essenciais para autenticação e segurança
 * da aplicação ProofChain, integrando UserDetailsService, encoder de senha e provider de autenticação.
 *
 * Estrutura atual:
 * Configuração baseada em Spring Security para autenticação via banco de dados (DAO)
 * com suporte a criptografia BCrypt e integração com fluxo JWT.
 *
 * Fluxo:
 * 1. Requisição de autenticação é recebida (login)
 * 2. AuthenticationManager inicia o processo de autenticação
 * 3. DaoAuthenticationProvider delega validação ao UserDetailsService
 * 4. PasswordEncoder valida hash da senha
 * 5. Usuário é autenticado ou rejeitado
 * 6. Fluxo segue para geração de JWT via AuthService
 *
 * Integração no sistema:
 * Base de suporte para autenticação dentro do ProofChain, atuando junto com SecurityConfig,
 * JwtService e AuthService.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityBeansConfig {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */
    private final UserDetailsServiceImpl userDetailsService;

    /*
     * =========================================================
     * PASSWORD ENCODER
     * =========================================================
     */

    /**
     * Responsável por criptografar e validar senhas utilizando BCrypt.
     *
     * @return PasswordEncoder configurado com BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * =========================================================
     * AUTHENTICATION PROVIDER
     * =========================================================
     */

    /**
     * Configura o provider responsável por autenticação baseada em banco de dados.
     *
     * @return DaoAuthenticationProvider configurado com UserDetailsService e PasswordEncoder
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /*
     * =========================================================
     * AUTHENTICATION MANAGER
     * =========================================================
     */

    /**
     * Expõe o AuthenticationManager utilizado no fluxo de autenticação.
     *
     * @param config configuração de autenticação do Spring
     * @return AuthenticationManager gerenciado pelo Spring
     * @throws Exception erro ao obter configuração de autenticação
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}