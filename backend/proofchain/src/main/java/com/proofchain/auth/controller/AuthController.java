package com.proofchain.auth.controller;

import com.proofchain.auth.dtos.AuthRequest;
import com.proofchain.auth.dtos.AuthResponse;
import com.proofchain.auth.dtos.NewPasswordDto;
import com.proofchain.auth.dtos.RefreshRequest;
import com.proofchain.auth.service.AuthService;
import com.proofchain.auth.service.ResetPasswordService;
import com.proofchain.user.applications.command.TokenValidationCommand;
import com.proofchain.user.interfaces.dto.request.TokenValidationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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

@Tag(
        name = "Authentication",
        description = "Endpoints responsáveis pela autenticação e geração de token JWT"
)
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ResetPasswordService resetPassword;

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
        AuthResponse retorno = authService.login(request);
        return ResponseEntity.ok(retorno);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshRequest request){
        AuthResponse retorno = authService.refreshToken(request);
        return ResponseEntity.ok(retorno);
    }

    /*
     * =========================================================
     * ATUALIZAÇÃO DE SENHA
     * =========================================================
     */

    /**
     * Troca de senha de um usuário
     *
     * param email identificador do usuário
     * param password nova senha do usuário
     * @return mensagem de confirmação
     */
    @Operation(
            summary = "Troca de senha de usuário",
            description = "Troca a senha de um usuário pertencente à instituição autenticada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha atualizada"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/reset/{email}")
    public ResponseEntity<String> resetPassword(@Valid @PathVariable String email) {

        resetPassword.resetPassword(email);
        return ResponseEntity.ok("Token enviado com sucesso.");
    }

    @PostMapping("/reset/validate")
    public ResponseEntity<String> tokenValidation(@Valid @RequestBody TokenValidationDto tokenValidation){
        TokenValidationCommand command = new TokenValidationCommand(tokenValidation);
        resetPassword.tokenValidation(command);
        return ResponseEntity.ok("Token validado com sucesso.");
    }

    @PatchMapping("/newPassword")
    public ResponseEntity<String> newPassword(@Valid @RequestBody NewPasswordDto newPassword){
        resetPassword.newPassword(newPassword);
        return ResponseEntity.ok("Senha alterada com sucesso.");
    }
}