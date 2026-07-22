package com.proofchain.user.applications.handler;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.UnauthorizedException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenatValidation;
import com.proofchain.user.applications.command.UpdateUserCommand;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * UpdateUserHandler
 *
 * Função no sistema:
 * Executa o caso de uso responsável pela atualização dos dados de um usuário,
 * garantindo que a operação ocorra exclusivamente dentro da instituição
 * autenticada (multi-tenant).
 *
 * Estrutura atual:
 * Handler da camada de aplicação seguindo o padrão CQRS leve adotado pelo
 * ProofChain. Toda atualização é realizada sobre usuários pertencentes ao
 * tenant autenticado.
 *
 * Fluxo:
 * 1. Obtém a instituição autenticada
 * 2. Valida autorização
 * 3. Valida existência da instituição
 * 4. Localiza o usuário
 * 5. Atualiza os dados permitidos
 * 6. Persiste as alterações
 * 7. Retorna o usuário atualizado
 *
 * Integração no sistema:
 * Utilizado pelo UserController durante o processo de atualização de usuários,
 * integrando InstitutionRepository, UserRepository e UserReturn.
 */
@Component
@AllArgsConstructor
public class UpdateUserHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final TenatValidation tenatValidation;

    /*
     * =========================================================
     * ATUALIZAÇÃO DE USUÁRIO
     * =========================================================
     */

    /**
     * Atualiza os dados de um usuário pertencente à instituição autenticada.
     *
     * Nota de decisão:
     * O institutionId será obtido futuramente através do SecurityContext,
     * impedindo alterações em usuários pertencentes a outras instituições.
     *
     * @param id identificador do usuário
     * @param command comando contendo os novos dados
     * @return usuário atualizado
     *
     * @throws .InstitutionNotAutorizedException quando não houver instituição autenticada
     * @throws .InstitutionNotFoundException quando a instituição não existir
     * @throws UserNotFoundException quando o usuário não for encontrado
     */
    public UserReturn updateUser(Long id, UpdateUserCommand command) {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);


        User user = userRepository
                .findByIdAndInstitution_Id(id, institutionId)
                .orElseThrow(()-> new NotFoundException(UserMessage.USER_NOT_FOUND));

        user.setName(command.getName());
        user.setRole(command.getRole());
        user.setActive(command.isActive());

        user = userRepository.save(user);

        return new UserReturn(user);
    }
}