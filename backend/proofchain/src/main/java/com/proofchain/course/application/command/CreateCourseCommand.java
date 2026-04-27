package com.proofchain.course.application.command;

import com.proofchain.course.interfaces.dto.request.CourseRequestDto;
import lombok.Getter;

@Getter
public class CreateCourseCommand {
    private final String name;
    private final String description;
    private final int hours;

    public CreateCourseCommand(CourseRequestDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.hours = dto.getHours();
    }
}
