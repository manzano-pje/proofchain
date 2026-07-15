package com.proofchain.instructor.interfaces.dto.response;

import com.proofchain.course.domain.model.Course;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.instructor.domain.model.Instructor;
import com.proofchain.user.domain.model.User;

import java.time.Instant;

public record InstructorReturn(
        Long id,
        User user,
        Course course,
        Instant createAt,
        Instant updateAt,
        Boolean isActive,
        Institution institution
){
    public static InstructorReturn from(Instructor instructor){
        return new InstructorReturn(
                instructor.getId(),
                instructor.getUser(),
                instructor.getCourse(),
                instructor.getCreateAt(),
                instructor.getUpdateAt(),
                instructor.isActive(),
                instructor.getInstitution()
        );
    }
}
