package com.proofchain.user.interfaces.dto.request;

import com.proofchain.user.domain.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserUpdateDto
 *
 * Função no sistema:
 * Representa os dados recebidos nas requisições de atualização de usuários da
 * plataforma ProofChain.
 *
 * Estrutura atual:
 * DTO pertencente à camada de interfaces, responsável exclusivamente pelo
 * transporte dos dados necessários para atualização de um usuário,
 * aplicando validações estruturais através do Bean Validation.
 *
 * Nota de decisão:
 * O e-mail do usuário não faz parte deste DTO, pois atualmente a plataforma
 * não permite sua alteração após o cadastro. Caso este requisito seja
 * implementado futuramente, deverá ser criado um caso de uso específico para
 * alteração de e-mail.
 *
 * Fluxo:
 * 1. Cliente envia a requisição de atualização
 * 2. Spring executa as validações declaradas neste DTO
 * 3. O Controller converte o DTO em UpdateUserCommand
 * 4. O Handler executa o caso de uso
 *
 * Integração no sistema:
 * Utilizado pelo UserController durante a atualização de usuários e convertido
 * para UpdateUserCommand na camada de aplicação.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    /*
     * =========================================================
     * DADOS ATUALIZÁVEIS
     * =========================================================
     */

    /**
     * Nome do usuário.
     */
    @Schema(
            description = "Nome completo do usuário.",
            example = "Paulo Manzano"
    )
    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 150, message = "O nome deve possuir no máximo 150 caracteres.")
    private String name;

    /**
     * Perfil de acesso do usuário.
     */
    @Schema(
            description = "Perfil de acesso do usuário.",
            example = "ADMIN"
    )
    @NotNull(message = "O perfil de acesso é obrigatório.")
    private UserRole role;

    /**
     * Situação do usuário na plataforma.
     */
    @Schema(
            description = "Indica se o usuário está ativo na plataforma.",
            example = "true"
    )
    private boolean isActive;
}