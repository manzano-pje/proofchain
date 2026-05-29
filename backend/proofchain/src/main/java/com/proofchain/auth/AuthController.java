package com.proofchain.auth;

import com.proofchain.shared.security.JwtService;
import com.proofchain.user.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // Endpoint de login
    @RequestMapping("/login")
    public AuthResponse login (@RequestBody AuthRequest authRequest){
        // cria objeto de autenticação com e-mail e password
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getEmail(),
                                authRequest.getPassword()
                        )
                );

        // Recupera o usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // GEra Token JWT
        String token = jwtService.generateToken(userDetails.getUser());

        return new AuthResponse(token);
    }
}


/*
🔧 Versão mais otimizada (sênior):
- Tratar exceções de login
- Rate limit
- Login audit
Motivo: segurança e observabilidade
*/
