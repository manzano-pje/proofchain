package com.proofchain.business.course.interfaces.dto.response;

import com.proofchain.business.course.domain.model.Course;

/**
 * CourseResponse
 *
 * Função no sistema:
 * Representa o objeto de resposta utilizado para expor dados de um curso
 * para a camada externa (API), ocultando detalhes internos da entidade de domínio.
 *
 * Estrutura atual:
 * Record imutável contendo:
 * - name: nome do curso
 * - description: descrição do curso
 * - hours: carga horária do curso
 *
 * Fluxo:
 * 1. Entidade Course é recuperada pela camada de aplicação
 * 2. CourseResponse é construído a partir da entidade
 * 3. Dados são retornados ao cliente via Controller
 *
 * Integração no sistema:
 * Utilizado como contrato de resposta do módulo de cursos na API REST.
 */
public record CourseResponse(
        String name,
        String description,
        int hours
){

    public CourseResponse(Course course) {
        this(
                course.getName(),
                course.getDescription(),
                course.getHours()
        );
    }
}