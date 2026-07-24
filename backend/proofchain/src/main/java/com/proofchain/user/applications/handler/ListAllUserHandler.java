package com.proofchain.user.applications.handler;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ListAllUserHandler
 *
 * Função no sistema:
 * Executa o caso de uso responsável pela consulta de todos os usuários
 * pertencentes à instituição autenticada, garantindo o isolamento de dados
 * entre tenants da plataforma ProofChain.
 *
 * Estrutura atual:
 * Handler da camada de aplicação seguindo o padrão CQRS leve adotado pelo
 * ProofChain. Todas as consultas são realizadas considerando o contexto da
 * instituição autenticada.
 *
 * Fluxo:
 * 1. Obtém a instituição autenticada
 * 2. Valida autorização do usuário
 * 3. Valida existência da instituição
 * 4. Recupera os usuários pertencentes ao tenant
 * 5. Converte as entidades para DTO de resposta
 * 6. Retorna a coleção ao controller
 *
 * Integração no sistema:
 * Utilizado pelo UserController para listagem de usuários, integrando
 * InstitutionRepository, UserRepository e UserReturn.
 */
@Component
@AllArgsConstructor
public class ListAllUserHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final TenantValidation tenantValidation;

    /*
     * =========================================================
     * LISTAGEM DE USUÁRIOS
     * =========================================================
     */

    /**
     * Retorna todos os usuários pertencentes à instituição autenticada.
     *
     * Nota de decisão:
     * O institutionId será obtido futuramente através do SecurityContext,
     * garantindo que cada instituição visualize exclusivamente seus próprios
     * usuários.
     *
     * @return lista de usuários da instituição autenticada
     *
     * @throws .InstitutionNotAutorizedException quando não houver instituição autenticada
     * @throws .InstitutionNotFoundException quando a instituição não existir
     * @throws UserNotFoundException quando não existirem usuários cadastrados
     */
    public List<UserReturn> listAllUser() {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);


        List<User> users = userRepository
                .findAllByInstitution_IdAndInstitution_DeletedAtIsNull(institutionId);

        if (users.isEmpty()) {
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
        }

        return users.stream()
                .map(UserReturn::new)
                .toList();
    }
}