package com.proofchain.business.instructor.interfaces.dto.response;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.business.instructor.domain.model.Instructor;
import com.proofchain.user.domain.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.Instant;

public record InstructorResponse(
        Long idInstructor,
        String name,
        String email,
        boolean isActive,
        Instant creatAt,
        Instant deletedAt
) {

    public InstructorResponse(Instructor instructor) {
        this(
                instructor.getIdInstructor(),
                instructor.getUser().getName(),
                instructor.getUser().getEmail(),
                instructor.isActive(),
                instructor.getCreatedAt(),
                instructor.getDeletedAt()
        );
    }
}