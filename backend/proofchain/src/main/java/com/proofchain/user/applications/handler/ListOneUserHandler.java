package com.proofchain.user.applications.handler;

import com.proofchain.admin.institution.domain.exception.InstitutionNotAutorizedException;
import com.proofchain.admin.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * ListOneUserHandler
 *
 * Função no sistema:
 * Executa o caso de uso responsável pela consulta de um único usuário,
 * garantindo que a busca seja realizada exclusivamente dentro da instituição
 * autenticada (multi-tenant).
 *
 * Estrutura atual:
 * Handler da camada de aplicação seguindo o padrão CQRS leve adotado pelo
 * ProofChain. Toda consulta é limitada ao tenant autenticado.
 *
 * Fluxo:
 * 1. Obtém a instituição autenticada
 * 2. Valida autorização
 * 3. Valida existência da instituição
 * 4. Localiza o usuário pelo e-mail dentro da instituição
 * 5. Converte a entidade para DTO de resposta
 * 6. Retorna o usuário ao controller
 *
 * Integração no sistema:
 * Utilizado pelo UserController para consulta individual de usuários,
 * integrando InstitutionRepository, UserRepository e UserReturn.
 */
@Component
@AllArgsConstructor
public class ListOneUserHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */

    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;

    /*
     * =========================================================
     * CONSULTA DE USUÁRIO
     * =========================================================
     */

    /**
     * Localiza um usuário pelo e-mail dentro da instituição autenticada.
     *
     * Nota de decisão:
     * O institutionId será obtido futuramente através do SecurityContext,
     * impedindo consultas entre instituições diferentes e garantindo o
     * isolamento dos dados da plataforma.
     *
     * @param email e-mail do usuário a ser localizado
     * @return dados do usuário encontrado
     *
     * @throws InstitutionNotAutorizedException quando não houver instituição autenticada
     * @throws InstitutionNotFoundException quando a instituição não existir
     * @throws UserNotFoundException quando o usuário não for encontrado
     */
    public UserReturn listOneUser(String email) {

        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();

        if (institutionId == null) {
            throw new InstitutionNotAutorizedException();
        }

        boolean existInstitution = institutionRepository
                .existsByIdAndDeletedAtIsNull(institutionId);

        if (!existInstitution) {
            throw new InstitutionNotFoundException();
        }

        User user = userRepository
                .findByEmailAndInstitution_Id(email, institutionId)
                .orElseThrow(UserNotFoundException::new);

        return new UserReturn(user);
    }
}