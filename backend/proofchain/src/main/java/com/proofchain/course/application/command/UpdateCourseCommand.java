package com.proofchain.course.application.command;

import com.proofchain.course.interfaces.dto.request.CourseRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCourseCommand {
    private  String name;
    private  String description;
    private  int hours;

    public UpdateCourseCommand(CourseRequestDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.hours = dto.getHours();
    }
}
