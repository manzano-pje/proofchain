package com.proofchain.business.course.application.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.application.command.CreateCourseCommand;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.CourseMessages;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * CreateCourseHandler
 *
 * Função no sistema:
 * Responsável por orquestrar o caso de uso de criação de curso dentro da camada de aplicação.
 * Executa validações de negócio, resolve dependências de domínio (Institution) e persiste a entidade Course.
 *
 * Estrutura atual:
 * Componente Spring da camada de aplicação (Application Layer).
 * Depende de CourseRepository e InstitutionRepository para validação e persistência.
 *
 * Fluxo:
 * 1. Recebe CreateCourseCommand da camada de aplicação
 * 2. Resolve o contexto da instituição (tenant)
 * 3. Valida existência da instituição
 * 4. Verifica duplicidade de curso por nome dentro da instituição
 * 5. Cria entidade Course via factory method
 * 6. Persiste entidade no repositório
 *
 * Integração no sistema:
 * Atua entre domínio e infraestrutura, sendo responsável por aplicar regras de negócio
 * do caso de uso de criação de cursos.
 */
@Component
@AllArgsConstructor
public class CreateCourseHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */
    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;

    /**
     * Executa o caso de uso de criação de curso.
     *
     * @param command dados necessários para criação do curso
     */
    public void handle(CreateCourseCommand command) {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        assert institutionId != null;

        Institution institution = institutionRepository
                .findByIdAndDeletedAtIsNull(institutionId)
                .orElseThrow(() -> new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND));

        /*
         * =========================================================
         * VALIDAÇÃO DE REGRA DE NEGÓCIO
         * =========================================================
         */

        boolean exists = courseRepository.existsByNameAndInstitutionId(
                command.getName(),
                institution.getId()
        );

        if (exists) {
            throw new AlreadyExistsException(CourseMessages.COURSE_ALREADY_EXISTS);
        }

        /*
         * =========================================================
         * CRIAÇÃO DA ENTIDADE DE DOMÍNIO
         * =========================================================
         */

        Course course = Course.create(
                command.getName(),
                command.getDescription(),
                command.getHours(),
                institution
        );

        /*
         * =========================================================
         * PERSISTÊNCIA
         * =========================================================
         */

        courseRepository.save(course);
    }
}