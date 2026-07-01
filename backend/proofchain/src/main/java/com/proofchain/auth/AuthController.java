package com.proofchain.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController (ProofChain - Security Boundary Layer)
 *
 * Responsabilidade:
 * - Expor endpoints HTTP relacionados à autenticação
 * - Validar entrada estrutural via Bean Validation
 * - Delegar toda lógica de autenticação ao AuthService
 *
 * NÃO é responsabilidade desta camada:
 * - Validar credenciais de usuário
 * - Gerar tokens JWT
 * - Acessar banco de dados
 * - Aplicar regras de negócio
 *
 * Fluxo de autenticação:
 * 1. Recebe AuthRequest (username/password)
 * 2. Encaminha para AuthService
 * 3. AuthService autentica via Spring Security
 * 4. JWT é gerado e retornado
 * 5. Response devolvida ao cliente
 */
@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "Camada responsável pelos endpoints de autenticação e geração de token JWT"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Autentica um usuário no sistema.
     *
     * Este endpoint valida credenciais (username e password),
     * e retorna um token JWT caso a autenticação seja bem-sucedida.
     *
     * O token retornado deve ser utilizado nas requisições subsequentes
     * no header Authorization como: Bearer <token>
     *
     * @param request credenciais de login do usuário
     * @return AuthResponse contendo o JWT gerado
     */
    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza autenticação do usuário no sistema e gera um token JWT válido para acesso às rotas protegidas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (payload incorreto)"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou usuário não autorizado")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}