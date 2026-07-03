package com.proofchain.user.applications.command;

import com.proofchain.user.domain.model.UserRole;
import com.proofchain.user.interfaces.dto.request.UserUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UpdateUserCommand
 *
 * Função no sistema:
 * Representa o comando responsável por encapsular os dados necessários para atualização
 * de um usuário na camada de aplicação do ProofChain.
 *
 * Estrutura atual:
 * Objeto de transferência utilizado entre a camada de interface e a camada de aplicação,
 * contendo apenas os dados permitidos para alteração.
 *
 * Fluxo:
 * 1. Controller recebe UserUpdateDto
 * 2. DTO é convertido para UpdateUserCommand
 * 3. Handler processa o comando
 * 4. Entidade User aplica as alterações de domínio
 *
 * Integração no sistema:
 * Utilizado pelos handlers responsáveis pelo caso de uso de atualização de usuários,
 * mantendo desacoplamento entre interface e regras de negócio.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserCommand {

    /*
     * =========================================================
     * DADOS PARA ATUALIZAÇÃO
     * =========================================================
     */

    private String name;
    private UserRole role;
    private boolean isActive;

    /**
     * Nota de decisão:
     * O Command representa exclusivamente os dados necessários para o caso de uso,
     * evitando que a camada de aplicação dependa diretamente dos DTOs da interface.
     *
     * @param dto objeto de entrada recebido pela camada de interface
     */
    public UpdateUserCommand(UserUpdateDto dto) {
        this.name = dto.getName();
        this.role = dto.getRole();
        this.isActive = dto.isActive();
    }
}