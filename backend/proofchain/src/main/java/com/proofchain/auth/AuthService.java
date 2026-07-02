package com.proofchain.auth;

import com.proofchain.shared.security.JwtService;
import com.proofchain.shared.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * AuthService
 *
 * Função no sistema:
 * Responsável por orquestrar o processo de autenticação de usuários.
 * Atua como camada de serviço entre o AuthController e o Spring Security,
 * delegando a validação de credenciais ao AuthenticationManager e a geração
 * de tokens ao JwtService.
 *
 * Estrutura atual:
 * Service stateless baseado em Spring Security.
 * Utiliza AuthenticationManager para validação de credenciais e JwtService para geração de JWT.
 *
 * Fluxo:
 * 1. Recebe AuthRequest do AuthController
 * 2. Encaminha credenciais ao AuthenticationManager para autenticação
 * 3. Recupera o principal autenticado (UserDetailsImpl)
 * 4. Gera token JWT via JwtService
 * 5. Retorna AuthResponse contendo o token gerado
 *
 * Integração no sistema:
 * Atua como núcleo do fluxo de autenticação,
 * integrando camada HTTP (Controller) com camada de segurança (Spring Security + JWT).
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}