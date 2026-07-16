package com.proofchain.business.couseClass.interfaces.dto.response;

import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.couseClass.domain.model.CourseClass;
import com.proofchain.admin.institution.domain.model.Institution;
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
    public static CourseClassReturn from(CourseClass courseClass){
        return new CourseClassReturn(
                courseClass.getId(),
                courseClass.getUser(),
                courseClass.getCourse(),
                courseClass.getCreateAt(),
                courseClass.getUpdateAt(),
                courseClass.isActive(),
                courseClass.getInstitution()
        );
    }
}
