package com.proofchain.course.application.handler;

import com.proofchain.course.application.command.CreateCourseCommand;
import com.proofchain.course.domain.exception.CourseIsRegisteredException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
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

//        Long institutionId = SecurityUtils.getInstitutionId();
        Long institutionId = 1L;

        Institution institution = institutionRepository
                .findByIdAndDeletedAtIsNull(institutionId)
                .orElseThrow(InstitutionNotFoundException::new);

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
            throw new CourseIsRegisteredException();
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