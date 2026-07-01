package com.proofchain.auth;

import com.proofchain.shared.security.JwtService;
import com.proofchain.shared.security.UserDetailsImpl;
import com.proofchain.shared.security.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService (ProofChain Core Security Layer)

 * Responsabilidade:
 * - Orquestrar o fluxo de autenticação de usuário
 * - Validar credenciais via Spring Security
 * - Gerar token JWT após autenticação bem-sucedida

 * Fluxo:
 * 1. Recebe AuthRequest (username/password)
 * 2. Valida credenciais via AuthenticationManager
 * 3. Carrega UserDetails autenticado
 * 4. Gera JWT via JwtService
 * 5. Retorna AuthResponse com token

 * NÃO é responsabilidade desta camada:
 * - Expor endpoints HTTP
 * - Validar payload de request
 * - Regras de negócio de domínio
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetailsImpl user =
                (UserDetailsImpl) userDetailsService.loadUserByUsername(request.username());

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}