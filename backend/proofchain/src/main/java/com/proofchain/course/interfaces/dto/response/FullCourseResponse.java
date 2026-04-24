package com.proofchain.course.interfaces.dto.response;

import com.proofchain.course.domain.model.Course;

import java.time.Instant;

public record FullCourseResponse (
     String name,
     String description,
     int hours,
     Instant createdAt,
     Instant updatedAt
){
    public FullCourseResponse(Course course){
        this(
                course.getName(),
                course.getDescription(),
                course.getHours(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
