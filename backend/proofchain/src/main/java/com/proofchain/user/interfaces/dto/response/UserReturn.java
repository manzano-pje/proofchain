package com.proofchain.user.interfaces.dto.response;

import com.proofchain.user.domain.model.User;
import com.proofchain.user.domain.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * UserReturn
 *
 * Função no sistema:
 * Representa os dados retornados ao cliente nas operações de consulta de
 * usuários da plataforma ProofChain.
 *
 * Estrutura atual:
 * DTO imutável (Record) pertencente à camada de interfaces, responsável
 * exclusivamente pela serialização das informações que podem ser expostas
 * pela API.
 *
 * Nota de decisão:
 * Este DTO não expõe informações sensíveis como senha, identificadores
 * internos, Institution, Tenant ou demais dados de infraestrutura.
 *
 * Fluxo:
 * 1. O Handler recupera a entidade User.
 * 2. A entidade é convertida para UserReturn.
 * 3. O Controller retorna o DTO ao cliente.
 *
 * Integração no sistema:
 * Utilizado pelos casos de uso de consulta de usuários, garantindo o
 * desacoplamento entre a entidade de domínio e a camada de apresentação.
 *
 * TODO (ProofChain):
 * Avaliar futuramente a inclusão do campo "updatedAt" quando houver
 * necessidade de auditoria nas respostas da API.
 */
public record UserReturn(

        @Schema(
                description = "Nome completo do usuário.",
                example = "Paulo Manzano"
        )
        String name,

        @Schema(
                description = "Endereço de e-mail utilizado pelo usuário.",
                example = "usuario@empresa.com.br"
        )
        String email,

        @Schema(
                description = "Perfil de acesso do usuário.",
                example = "ADMIN"
        )
        UserRole role,

        @Schema(
                description = "Indica se o usuário está ativo na plataforma.",
                example = "true"
        )
        boolean active,

        @Schema(
                description = "Data e hora de criação do usuário.",
                example = "2026-07-02T15:30:00Z"
        )
        Instant createAt

//      @Schema(
//              description = "Data da última atualização do usuário."
//      )
//      Instant updateAt

) {

    /*
     * =========================================================
     * CONSTRUTORES
     * =========================================================
     */

    /**
     * Converte uma entidade de domínio em um DTO de resposta.
     *
     * Responsabilidade:
     * Isolar a camada de apresentação da estrutura interna da entidade
     * User, expondo apenas os dados necessários ao cliente.
     *
     * @param user entidade de domínio recuperada pela camada de aplicação.
     */
    public UserReturn(User user) {
        this(
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreateAt()
//              user.getUpdateAt()
        );
    }
}