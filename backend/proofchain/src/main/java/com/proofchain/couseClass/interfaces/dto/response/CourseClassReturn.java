package com.proofchain.couseClass.interfaces.dto.response;

import com.proofchain.course.domain.model.Course;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.couseClass.domain.model.Instructor;
import com.proofchain.user.domain.model.User;

import java.time.Instant;

public record CourseClassReturn(
        Long id,
        User user,
        Course course,
        Instant createAt,
        Instant updateAt,
        Boolean isActive,
        Institution institution
){
    public static CourseClassReturn from(Instructor instructor){
        return new CourseClassReturn(
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
