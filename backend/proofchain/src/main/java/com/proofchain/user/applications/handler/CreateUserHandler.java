package com.proofchain.user.applications.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.UnauthorizedException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.exception.messages.InstructorMessages;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.user.applications.command.CreateUserCommand;
import com.proofchain.user.domain.exception.UserRegisteredException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * CreateUserHandler
 *
 * Função no sistema:
 * Executa o caso de uso responsável pela criação de usuários, garantindo a
 * validação da instituição, unicidade do usuário dentro do tenant e
 * persistência da entidade no banco de dados.
 *
 * Estrutura atual:
 * Handler da camada de aplicação seguindo o padrão CQRS leve adotado pelo
 * ProofChain. Toda operação ocorre no contexto da instituição obtida através
 * da autenticação (multi-tenant).
 *
 * Fluxo:
 * 1. Recebe CreateUserCommand
 * 2. Obtém a instituição autenticada
 * 3. Valida existência da instituição
 * 4. Verifica duplicidade do usuário
 * 5. Criptografa a senha
 * 6. Cria a entidade User
 * 7. Persiste o usuário
 *
 * Integração no sistema:
 * Utilizado pelo UserController durante o cadastro de usuários, integrando
 * InstitutionRepository, UserRepository e PasswordEncoder.
 */
@Component
@AllArgsConstructor
public class CreateUserHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InstitutionRepository institutionRepository;

    /*
     * =========================================================
     * CRIAÇÃO DE USUÁRIO
     * =========================================================
     */

    /**
     * Realiza o cadastro de um novo usuário na instituição autenticada.
     *
     * Nota de decisão:
     * O institutionId será obtido futuramente através do SecurityContext,
     * eliminando qualquer possibilidade de o cliente informar manualmente
     * a instituição no request, preservando o isolamento multi-tenant.
     *
     * @param command comando contendo os dados necessários para criação do usuário
     *
     * @throws .InstitutionNotAutorizedException quando não existir instituição autenticada
     * @throws .InstitutionNotFoundException quando a instituição não existir
     * @throws UserRegisteredException quando já existir usuário com o mesmo nome na instituição
     */
    public void createUser(CreateUserCommand command) {

        // 🔑 Instituição obtida do contexto de segurança.
        Long institutionId = SecurityUtils.getInstitutionId();

        if (institutionId == null) {
            throw new UnauthorizedException(InstitutionMessages.INSTITUTION_NOT_AUTORIZED);
        }

        Institution institution = institutionRepository
                .findByIdAndDeletedAtIsNull(institutionId)
                .orElseThrow(()-> new NotFoundException(InstructorMessages.INSTRUCTOR_NOT_FOUND));

        boolean existUser = userRepository.existsByEmailAndInstitutionId(
                command.getEmail(),
                institutionId
        );

        if (existUser) {
            throw new AlreadyExistsException(UserMessage.USER_ALREADY_EXISTS);
        }

        User user = User.create(
                command.getName(),
                command.getEmail(),
                passwordEncoder.encode(command.getPassword()),
                command.getRole(),
                institution
        );

        userRepository.save(user);
    }
}