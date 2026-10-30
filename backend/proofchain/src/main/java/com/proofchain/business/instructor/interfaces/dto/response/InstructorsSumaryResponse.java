package com.proofchain.business.instructor.interfaces.dto.response;

import com.proofchain.business.instructor.domain.model.Instructor;

import java.time.Instant;

public record InstructorsSumaryResponse (
    Long id,
    String name,
    String email
){

    public InstructorsSumaryResponse(Instructor instructor) {
        this(
                instructor.getIdInstructor(),
                instructor.getUser().getName(),
                instructor.getUser().getEmail()
        );
    }
}
