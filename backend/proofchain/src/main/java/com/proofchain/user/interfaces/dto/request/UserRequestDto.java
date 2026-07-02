package com.proofchain.user.interfaces.dto.request;

import com.proofchain.user.domain.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserRequestDto
 *
 * Função no sistema:
 * Representa os dados recebidos nas requisições de cadastro de usuários da
 * plataforma ProofChain.
 *
 * Estrutura atual:
 * DTO pertencente à camada de interfaces, responsável exclusivamente pelo
 * transporte de dados entre o cliente e a API, aplicando validações
 * estruturais através do Bean Validation.
 *
 * Fluxo:
 * 1. Cliente envia a requisição de cadastro
 * 2. Spring realiza as validações declaradas neste DTO
 * 3. O Controller converte o DTO em CreateUserCommand
 * 4. O Handler executa o caso de uso
 *
 * Integração no sistema:
 * Utilizado pelo UserController durante o cadastro de usuários e convertido
 * para CreateUserCommand na camada de aplicação.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {

    /*
     * =========================================================
     * DADOS DO USUÁRIO
     * =========================================================
     */

    /**
     * Nome completo do usuário.
     */
    @Schema(
            description = "Nome completo do usuário.",
            example = "Paulo Manzano"
    )
    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 150, message = "O nome deve possuir no máximo 150 caracteres.")
    private String name;

    /**
     * Endereço de e-mail utilizado como login.
     */
    @Schema(
            description = "E-mail utilizado para autenticação.",
            example = "usuario@empresa.com.br"
    )
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 150, message = "O e-mail deve possuir no máximo 150 caracteres.")
    private String email;

    /**
     * Senha de acesso do usuário.
     *
     * Nota de decisão:
     * A senha será criptografada pelo PasswordEncoder antes
     * de ser persistida no banco de dados.
     */
    @Schema(
            description = "Senha de acesso do usuário.",
            example = "Senha@123"
    )
    @NotBlank(message = "A senha é obrigatória.")
    @Size(
            min = 8,
            max = 100,
            message = "A senha deve possuir entre 8 e 100 caracteres."
    )
    private String password;

    /**
     * Perfil de acesso do usuário.
     */
    @Schema(
            description = "Perfil de acesso do usuário.",
            example = "ADMIN"
    )
    @NotNull(message = "O perfil de acesso é obrigatório.")
    private UserRole role;
}