package com.proofchain.business.course.interfaces.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CourseRequestDto
 *
 * Função no sistema:
 * Representa o objeto de transferência de dados utilizado para criação e atualização de cursos
 * via camada de interface (Controller).
 *
 * Estrutura atual:
 * DTO mutável contendo os dados básicos de um curso:
 * - id: identificador do curso (utilizado em operações de update)
 * - name: nome do curso
 * - description: descrição do curso
 * - hours: carga horária do curso
 *
 * Fluxo:
 * 1. Requisição HTTP é recebida pelo CourseController
 * 2. JSON é convertido automaticamente para CourseRequestDto
 * 3. DTO é transformado em Command (CreateCourseCommand / UpdateCourseCommand)
 * 4. Command é processado pela camada de aplicação
 *
 * Integração no sistema:
 * Utilizado exclusivamente pela camada de interface para entrada de dados do módulo de cursos.
 */

public record CourseRequestDto(

    Long id,

    @Size(max = 100)
    String name,

    @Size(max = 200)
    String description,

    int hours
) {}