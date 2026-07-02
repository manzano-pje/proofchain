package com.proofchain.course.application.handler;

import com.proofchain.course.application.command.UpdateCourseCommand;
import com.proofchain.course.domain.exception.CourseIsRegisteredException;
import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * UpdateCourseHandler
 *
 * Função no sistema:
 * Responsável por executar o caso de uso de atualização de curso dentro do contexto de uma instituição.
 * Orquestra validações de existência, regras de negócio e persistência da entidade Course.
 *
 * Estrutura atual:
 * Componente da camada de aplicação (Application Layer).
 * Depende de CourseRepository e InstitutionRepository para validação de contexto e persistência.
 *
 * Fluxo:
 * 1. Obtém o contexto da instituição (tenant via SecurityContext futuro)
 * 2. Valida existência da instituição no sistema
 * 3. Busca o curso por ID dentro da instituição
 * 4. Valida existência do curso
 * 5. Valida regra de unicidade de nome de curso dentro da instituição
 * 6. Atualiza entidade Course com novos dados
 * 7. Persiste alterações no repositório
 *
 * Integração no sistema:
 * Utilizado pela camada de interface (Controller) para atualização de cursos
 * dentro do escopo da instituição (tenant).
 */
@AllArgsConstructor
@Component
public class UpdateCourseHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */
    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;

    /**
     * Executa o caso de uso de atualização de curso.
     *
     * @param id identificador do curso a ser atualizado
     * @param command dados atualizados do curso
     * @return Course entidade atualizada
     */
    public Course updateCourse(Long id, UpdateCourseCommand command) {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        // Long institutionId = SecurityUtils.getInstitutionId();
        Long institutionId = 1L;

        Institution institution = institutionRepository
                .findById(institutionId)
                .orElseThrow(InstitutionNotFoundException::new);

        /*
         * =========================================================
         * CONSULTA DE DOMÍNIO
         * =========================================================
         */

        Course course = courseRepository
                .findByIdAndInstitutionId(id, institutionId)
                .orElseThrow(CourseNotFoundException::new);

        /*
         * =========================================================
         * VALIDAÇÃO DE REGRA DE NEGÓCIO
         * =========================================================
         */

        boolean exists = courseRepository.existsByIdAndInstitutionId(
                command.getId(),
                institution.getId()
        );

        if (exists && !course.getName().equals(command.getName())) {
            throw new CourseIsRegisteredException();
        }

        /*
         * =========================================================
         * ATUALIZAÇÃO DE ENTIDADE
         * =========================================================
         */

        course.updateCourse(
                command.getName(),
                command.getDescription(),
                command.getHours()
        );

        /*
         * =========================================================
         * PERSISTÊNCIA
         * =========================================================
         */

        courseRepository.save(course);

        return course;
    }
}