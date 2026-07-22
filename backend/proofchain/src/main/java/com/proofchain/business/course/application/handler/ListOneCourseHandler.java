package com.proofchain.business.course.application.handler;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.business.course.interfaces.dto.response.CourseResponse;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.CourseMessages;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenatValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * ListOneCourseHandler
 *
 * Função no sistema:
 * Responsável por executar o caso de uso de consulta de um curso específico por ID dentro do contexto de uma instituição.
 * Atua como orquestrador da busca de um único registro de Course no sistema.
 *
 * Estrutura atual:
 * Componente da camada de aplicação (Application Layer).
 * Depende de CourseRepository e InstitutionRepository para validação de contexto e acesso a dados.
 *
 * Fluxo:
 * 1. Obtém o contexto da instituição (tenant)
 * 2. Valida existência da instituição no sistema
 * 3. Busca o curso por ID dentro da instituição
 * 4. Valida existência do curso
 * 5. Converte entidade Course para CourseResponse
 * 6. Retorna o curso solicitado
 *
 * Integração no sistema:
 * Utilizado pela camada de interface (Controller) para consulta individual de cursos
 * filtrados pelo contexto da instituição.
 */
@Component
@AllArgsConstructor
public class ListOneCourseHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */
    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;
    private final TenatValidation tenatValidation;

    /**
     * Executa o caso de uso de consulta de um curso por ID.
     *
     * @param id identificador do curso
     * @return CourseResponse com os dados do curso encontrado
     */
    public CourseResponse listOneCourse(Long id) {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);


        /*
         * =========================================================
         * CONSULTA DE DOMÍNIO
         * =========================================================
         */

        Optional<Course> courseOptional = courseRepository
                .findByIdAndInstitutionId(id, institutionId);

        if (courseOptional.isEmpty()) {
            throw new NotFoundException(CourseMessages.COURSE_NOT_FOUND);
        }

        /*
         * =========================================================
         * MAPEAMENTO DE RESPOSTA
         * =========================================================
         */

        return new CourseResponse(courseOptional.get());
    }
}