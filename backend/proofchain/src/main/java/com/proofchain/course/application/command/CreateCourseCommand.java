package com.proofchain.course.application.command;

import com.proofchain.course.interfaces.dto.request.CourseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * CreateCourseCommand
 *
 * Função no sistema:
 * Representa o comando de criação de um curso dentro da camada de aplicação.
 * Atua como objeto intermediário entre a camada de interface (DTO) e a camada de domínio,
 * encapsulando os dados necessários para execução da operação de criação de curso.
 *
 * Estrutura atual:
 * Classe imutável contendo os dados essenciais para criação de um curso:
 * - name: nome do curso
 * - description: descrição do curso
 * - hours: carga horária do curso
 *
 * Fluxo:
 * 1. CourseRequestDto é recebido pela camada de interface
 * 2. DTO é convertido para CreateCourseCommand
 * 3. Command é processado pela camada de aplicação (use case / service)
 *
 * Integração no sistema:
 * Utilizado pela camada de aplicação do módulo de cursos,
 * servindo como entrada estruturada para operações de criação de curso.
 */
@Getter
@AllArgsConstructor
public class CreateCourseCommand {

    private final String name;
    private final String description;
    private final int hours;

    public CreateCourseCommand(CourseRequestDto dto) {
        this.name = dto.name();
        this.description = dto.description();
        this.hours = dto.hours();
    }
}