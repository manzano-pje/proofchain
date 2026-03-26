package com.proofchain.Dtos.response;

import com.proofchain.identities.Course;

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
