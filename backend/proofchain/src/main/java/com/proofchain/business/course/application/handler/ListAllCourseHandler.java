package com.proofchain.business.course.application.handler;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.business.course.interfaces.dto.response.FullCourseResponse;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.CourseMessages;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ListAllCourseHandler
 *
 * Função no sistema:
 * Responsável por executar o caso de uso de listagem de todos os cursos de uma instituição.
 * Atua como orquestrador da consulta de dados de cursos dentro do contexto de tenant (instituição).
 *
 * Estrutura atual:
 * Componente da camada de aplicação (Application Layer).
 * Realiza consultas em CourseRepository e valida existência da Institution no sistema.
 *
 * Fluxo:
 * 1. Obtém o contexto da instituição (tenant)
 * 2. Valida existência da instituição
 * 3. Consulta todos os cursos associados à instituição
 * 4. Valida se existem cursos cadastrados
 * 5. Converte entidade Course para FullCourseResponse
 * 6. Retorna lista de cursos ao consumidor
 *
 * Integração no sistema:
 * Utilizado pela camada de interface (Controller) para fornecer listagem de cursos
 * filtrados por instituição.
 */
@Component
@AllArgsConstructor
public class ListAllCourseHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */
    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;

    /**
     * Executa o caso de uso de listagem de cursos por instituição.
     *
     * @return lista de cursos convertidos para response DTO
     */
    public List<FullCourseResponse> listAllCourses() {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

//        Long institutionId = SecurityUtils.getInstitutionId();
        Long institutionId = 1L;

        boolean existsInstitution = institutionRepository
                .existsByIdAndDeletedAtIsNull(institutionId);

        if (!existsInstitution) {
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }

        /*
         * =========================================================
         * CONSULTA DE DOMÍNIO
         * =========================================================
         */

        List<Course> courseList = courseRepository.findAllByInstitutionId(institutionId);

        if (courseList.isEmpty()) {
            throw new NotFoundException(CourseMessages.COURSE_NOT_FOUND);
        }

        /*
         * =========================================================
         * MAPEAMENTO DE RESPOSTA
         * =========================================================
         */

        return courseList.stream()
                .map(FullCourseResponse::new)
                .collect(Collectors.toList());
    }
}