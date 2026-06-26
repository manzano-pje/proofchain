package com.proofchain.auth;


import com.proofchain.shared.security.JwtService;
import com.proofchain.shared.security.UserDetailsImpl;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService
 *
 * Responsabilidade:
 * Executar o fluxo de login da aplicação, validando as credenciais do usuário.
 *
 * Função no sistema:
 * Verifica se o usuário existe e se a senha informada está correta,
 * retornando o resultado da tentativa de login.
 *
 * Fluxo:
 * 1. Recebe email e senha do AuthController
 * 2. Busca o usuário no repositório
 * 3. Valida a senha utilizando PasswordEncoder
 * 4. Retorna sucesso ou falha no login
 *
 * Integração no sistema:
 * Utilizado pelo AuthController no endpoint de login.
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest request) {
// 1. Tenta autenticar o usuário usando o mecanismo do Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. Se não lançar exceção, busca os detalhes do usuário
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        // 3. Verifica se o objeto retornado é de facto a sua classe concreta e faz o Cast
        if (userDetails instanceof UserDetailsImpl userDetailsImpl) {
            // Agora você tem acesso direto aos campos customizados (como getInstitutionId())
            return jwtService.generateToken(userDetailsImpl);
        }

        // 4. Retorna o token JWT gerado
        return jwtService.generateToken(userDetailsImpl, userDetailsImpl.getInstitutionId());

    }
}

