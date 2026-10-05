package com.proofchain.business.course.application.handler;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.CourseMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteCourseHandler {
    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */
    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;
    private final TenantValidation tenantValidation;

    /**
     * Executa o caso de uso de exclusão lógica de curso.
     *
     * @param id identificador do curso a ser atualizado
     * @return void
     */

    public void deleteCourse(Long id) {
        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);

        /*
         * =========================================================
         * CONSULTA DE DOMÍNIO
         * =========================================================
         */

        Course course = courseRepository
                .findByIdAndInstitutionId(id, institutionId)
                .orElseThrow(() -> new NotFoundException(CourseMessages.COURSE_NOT_FOUND));

        /*
         * =========================================================
         * VALIDAÇÃO DE REGRA DE NEGÓCIO
         * =========================================================
         */

        course.setDeletedAt(Instant.now());
        courseRepository.save(course);
    }
}
