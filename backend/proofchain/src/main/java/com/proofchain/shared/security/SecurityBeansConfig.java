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
 * Responsabilidade:
 * Configura e expõe os beans essenciais para o processo de autenticação e segurança
 * da aplicação utilizando Spring Security.
 *
 * Componentes configurados:
 *
 * - PasswordEncoder:
 *   Responsável pela criptografia e validação de senhas utilizando BCrypt,
 *   garantindo armazenamento seguro das credenciais.
 *
 * - AuthenticationProvider:
 *   Integra o UserDetailsService com o PasswordEncoder,
 *   executando a validação das credenciais no processo de autenticação.
 *
 * - AuthenticationManager:
 *   Orquestra o fluxo de autenticação, delegando a validação ao AuthenticationProvider.
 *
 * Fluxo de execução:
 * 1. Requisição de autenticação é iniciada
 * 2. AuthenticationManager coordena o processo
 * 3. AuthenticationProvider valida as credenciais
 * 4. PasswordEncoder compara hash da senha
 * 5. UserDetailsService carrega os dados do usuário
 * 6. Autenticação é concluída ou rejeitada
 *
 * Observação:
 * Esta configuração suporta o fluxo completo de autenticação da aplicação,
 * incluindo login, geração de JWT e controle de acesso baseado em roles.
 */

@Configuration
@RequiredArgsConstructor
public class SecurityBeansConfig {

    private final UserDetailsServiceImpl userDetailsService;

    // Responsável por encriptar a senha
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(
                userDetailsService
        );

        authProvider.setPasswordEncoder(
                passwordEncoder()
        );

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception{

        return config.getAuthenticationManager();
    }
}
