package com.proofchain.course.interfaces.dto.response;

import com.proofchain.course.domain.model.Course;

import java.time.Instant;

/**
 * FullCourseResponse
 *
 * Função no sistema:
 * Representa o objeto de resposta detalhado utilizado para expor informações completas de um curso
 * na API, incluindo metadados de auditoria (criação e atualização).
 *
 * Estrutura atual:
 * Record imutável contendo:
 * - name: nome do curso
 * - description: descrição do curso
 * - hours: carga horária do curso
 * - createdAt: data de criação do registro
 * - updatedAt: data da última atualização
 *
 * Fluxo:
 * 1. Entidade Course é recuperada pela camada de aplicação
 * 2. FullCourseResponse é construído a partir da entidade
 * 3. Dados completos são retornados ao cliente via Controller
 *
 * Integração no sistema:
 * Utilizado em endpoints de listagem (ex: listAllCourses) onde é necessário expor
 * informações completas do curso.
 */
public record FullCourseResponse (
        String name,
        String description,
        int hours,
        Instant createdAt,
        Instant updatedAt
){
    public FullCourseResponse(Course course){
        this(
                course.getName(),
                course.getDescription(),
                course.getHours(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}