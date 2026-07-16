package com.proofchain.business.course.application.command;

import com.proofchain.business.course.interfaces.dto.request.CourseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * UpdateCourseCommand
 *
 * Função no sistema:
 * Representa o comando de atualização de um curso dentro da camada de aplicação.
 * Atua como objeto de transporte entre a camada de interface (DTO) e a camada de domínio,
 * encapsulando os dados necessários para execução da operação de atualização de curso.
 *
 * Estrutura atual:
 * Classe mutável contendo os dados necessários para atualização de um curso:
 * - id: identificador do curso
 * - name: nome do curso
 * - description: descrição do curso
 * - hours: carga horária do curso
 *
 * Fluxo:
 * 1. CourseRequestDto é recebido pela camada de interface
 * 2. DTO é convertido para UpdateCourseCommand
 * 3. Command é processado pela camada de aplicação (service / use case)
 * 4. Entidade de curso é atualizada com os novos dados
 *
 * Integração no sistema:
 * Utilizado pela camada de aplicação do módulo de cursos,
 * servindo como entrada estruturada para operações de atualização de curso.
 */
@Getter
@Setter
@AllArgsConstructor
public class UpdateCourseCommand {

    private Long id;
    private String name;
    private String description;
    private int hours;

    public UpdateCourseCommand(CourseRequestDto dto) {
        this.id = dto.id();
        this.name = dto.name();
        this.description = dto.description();
        this.hours = dto.hours();
    }
}