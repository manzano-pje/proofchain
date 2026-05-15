package com.proofchain.course.interfaces.dto.response;

import com.proofchain.course.domain.model.Course;

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
