package com.proofchain.course.application.command;

import com.proofchain.course.interfaces.dto.request.CourseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
/**
 * Descrição concisa da responsabilidade da classe.
 * @author Arquiteto de Software Qwen
 * @since 2023
 */
public class CreateCourseCommand {
    private final String name;
    private final String description;
    private final int hours;

    public CreateCourseCommand(CourseRequestDto dto){
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.hours = dto.getHours();
    }
}
