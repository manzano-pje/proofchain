package com.proofchain.Dtos.response;

import com.proofchain.identities.Course;

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
