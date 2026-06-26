package com.proofchain.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController
 *
 * Responsabilidade:
 * Expor os endpoints responsáveis pelo processo de login da aplicação.
 *
 * Função no sistema:
 * Recebe as credenciais do usuário, encaminha para o AuthService e retorna
 * o resultado da tentativa de login.
 *
 * Fluxo:
 * 1. Cliente envia requisição POST com email e senha
 * 2. Controller recebe e mapeia para AuthRequest
 * 3. AuthService valida as credenciais
 * 4. Resultado do login é retornado ao cliente
 *
 * Integração no sistema:
 * Camada de entrada do fluxo de login, conectando API REST com a lógica de negócio.
 */

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(AuthRequest authRequest){
        AuthResponse response = authService.login(authRequest);

        return ResponseEntity.ok().build();
    }
}
