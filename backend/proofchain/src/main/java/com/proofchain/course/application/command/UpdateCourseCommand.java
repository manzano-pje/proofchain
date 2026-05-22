package com.proofchain.course.application.command;

import com.proofchain.course.interfaces.dto.request.CourseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
/**
 * Descrição concisa da responsabilidade da classe.
 * @author Arquiteto de Software Qwen
 * @since 2023
 */
public class UpdateCourseCommand {
    private Long id;
    private  String name;
    private  String description;
    private  int hours;

    public UpdateCourseCommand(CourseRequestDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.hours = dto.getHours();
    }
}
