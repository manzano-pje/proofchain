package com.proofchain.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 *
 * Função no sistema:
 * Responsável por expor endpoints HTTP de autenticação.
 * Atua como camada de entrada (Boundary Layer), delegando toda lógica de autenticação ao AuthService.
 *
 * Estrutura atual:
 * Controller REST stateless utilizando Spring Web.
 * Integração com Swagger para documentação de API.
 *
 * Fluxo:
 * 1. Recebe AuthRequest (username/password)
 * 2. Encaminha requisição para AuthService
 * 3. AuthService executa autenticação e gera JWT
 * 4. Token é retornado ao cliente via AuthResponse
 *
 * Integração no sistema:
 * Utilizado como ponto de entrada do módulo de autenticação.
 * Integra camada HTTP com camada de serviço de autenticação.
 */
@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "Endpoints responsáveis pela autenticação e geração de token JWT"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Realiza autenticação do usuário e gera token JWT.
     *
     * @param request credenciais de autenticação (username e password)
     * @return AuthResponse contendo JWT válido
     */
    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza autenticação e retorna token JWT para acesso às rotas protegidas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}