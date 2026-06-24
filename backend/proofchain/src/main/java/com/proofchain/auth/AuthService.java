package com.proofchain.auth;


import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request){

        var userOpt = userRepository.findByEmail(request.email());
        if(userOpt.isEmpty()){
            return new AuthResponse("Usuário não encontrado.", false);
        }

        var user = userOpt.get();
        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if(!passwordMatches){
            return new AuthResponse("Senha incorreta", false);
        }

        return new AuthResponse("Login efetuado com sucesso",true);
    }

}
