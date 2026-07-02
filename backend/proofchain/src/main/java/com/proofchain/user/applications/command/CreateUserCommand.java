package com.proofchain.user.applications.command;

import com.proofchain.user.domain.model.UserRole;
import com.proofchain.user.interfaces.dto.request.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CreateUserCommand
 *
 * Função no sistema:
 * Representa o comando responsável por encapsular os dados necessários para a criação de um usuário
 * dentro da camada de aplicação do ProofChain.
 *
 * Estrutura atual:
 * Objeto imutável que transporta dados do DTO (camada de interface) para a camada de aplicação,
 * desacoplando entrada externa da lógica de negócio.
 *
 * Fluxo:
 * 1. Controller recebe UserRequestDto
 * 2. DTO é convertido para CreateUserCommand
 * 3. Command é processado pelo handler de criação de usuário
 * 4. Dados são utilizados para criação da entidade User
 *
 * Integração no sistema:
 * Utilizado pela camada de aplicação (User handlers) como entrada padronizada de casos de uso
 * relacionados à criação de usuários.
 */
@Getter
@AllArgsConstructor
public class CreateUserCommand {

    /*
     * =========================================================
     * DADOS DO USUÁRIO
     * =========================================================
     */

    private String name;
    private String email;
    private String password;
    private UserRole role;

    /**
     * Nota de decisão:
     * A conversão direta do DTO para Command mantém a camada de aplicação desacoplada
     * da camada de interface, preservando o padrão CQRS leve adotado no ProofChain.
     *
     * @param dto objeto de entrada vindo da camada de interface
     */
    public CreateUserCommand(UserRequestDto dto) {
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.role = dto.getRole();
    }
}