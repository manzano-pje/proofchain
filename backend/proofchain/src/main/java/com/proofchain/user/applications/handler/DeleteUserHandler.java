package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * DeleteUserHandler
 *
 * Função no sistema:
 * Executa o caso de uso responsável pela exclusão de usuários, garantindo que
 * a operação ocorra apenas dentro da instituição autenticada e que o usuário
 * pertença ao tenant corrente.
 *
 * Estrutura atual:
 * Handler da camada de aplicação seguindo o padrão CQRS leve adotado pelo
 * ProofChain. Toda validação de isolamento multi-tenant é realizada antes da
 * remoção do registro.
 *
 * Fluxo:
 * 1. Obtém a instituição autenticada
 * 2. Valida existência da instituição
 * 3. Verifica se o usuário pertence à instituição
 * 4. Remove o usuário do banco de dados
 *
 * Integração no sistema:
 * Utilizado pelo UserController durante o processo de exclusão de usuários,
 * integrando InstitutionRepository e UserRepository.
 */
@Component
@AllArgsConstructor
public class DeleteUserHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    /*
     * =========================================================
     * EXCLUSÃO DE USUÁRIO
     * =========================================================
     */

    /**
     * Remove um usuário pertencente à instituição autenticada.
     *
     * Nota de decisão:
     * O institutionId será obtido futuramente através do SecurityContext,
     * garantindo que nenhuma operação possa ser executada sobre usuários de
     * outra instituição.
     *
     * @param id identificador do usuário a ser removido
     *
     * @throws InstitutionNotFoundException quando a instituição não existir
     * @throws UserNotFoundException quando o usuário não existir na instituição
     */
    public void deleteUser(Long id) {

        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();


        boolean existInstitution = institutionRepository
                .existsByIdAndDeletedAtIsNull(institutionId);

        if (!existInstitution) {
            throw new InstitutionNotFoundException();
        }

        User user = userRepository
                .findByIdAndInstitution_Id(id, institutionId)
                .orElseThrow(UserNotFoundException::new);

        user.setActive(false);
        userRepository.save(user);
    }
}