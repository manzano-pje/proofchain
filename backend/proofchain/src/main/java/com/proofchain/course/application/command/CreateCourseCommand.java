package com.proofchain.course.application.command;

import com.proofchain.course.interfaces.dto.request.CourseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class CreateCourseCommand {
    private final Long id;
    private final String name;
    private final String description;
    private final int hours;

    public CreateCourseCommand(CourseRequestDto dto){
        this.id = dto.getId();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.hours = dto.getHours();
    }
}
