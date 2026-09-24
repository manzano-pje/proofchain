package com.proofchain.auth.service;

import com.proofchain.auth.dtos.AuthRequest;
import com.proofchain.auth.dtos.AuthResponse;
import com.proofchain.auth.dtos.UserSummaryDto;
import com.proofchain.shared.exception.ForbiddenException;
import com.proofchain.shared.exception.UnauthorizedException;
import com.proofchain.shared.exception.messages.AuthMessages;
import com.proofchain.shared.security.JwtService;
import com.proofchain.shared.security.UserDetailsImpl;
import org.springframework.security.authentication.*;
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

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );
            UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

            // 1. Gera os tokens
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            // 2. AQUI A IDE RECONHECERÁ A CHAMADA:
            Long expiresIn = jwtService.getExpirationInSeconds();

            // 3. Monta os dados do usuário
            UserSummaryDto userSummary = new UserSummaryDto(
                    user.getId(),
                    user.getUsername(),
                    user.getUsername(),
                    user.getRole(),
                    user.getInstitutionId()
            );

            // 4. Retorna a resposta contendo o tempo em segundos (ex: 900)
            return new AuthResponse(
                    token,
                    refreshToken,
                    expiresIn, // <--- Passado para a resposta HTTP
                    userSummary
            );
        }catch (BadCredentialsException | InternalAuthenticationServiceException e){
            throw new UnauthorizedException(AuthMessages.INVALID_CREDENTIALS); // status 401

        }catch (AccountExpiredException e) {
            throw new AccountExpiredException(AuthMessages.ACCOUNT_EXPIRED); // status 403

        }catch (LockedException e) {
            throw new ForbiddenException(AuthMessages.ACCESS_DENIED); // status 403

        }catch (DisabledException e) {
            throw new ForbiddenException(AuthMessages.ACCOUNT_DISABLED); // status 403

        }catch (CredentialsExpiredException e) {
            throw new CredentialsExpiredException(AuthMessages.CREDENTIALS_EXPIRED); // status 403
        }
    }
}

