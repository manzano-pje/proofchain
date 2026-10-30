package com.proofchain.business.courseClass.interfaces.dto.response;

import com.proofchain.business.courseClass.domain.model.CourseClass;
import com.proofchain.business.participant.domain.model.Participant;

import java.time.Instant;
import java.util.List;

public record CourseClassReturn(
        Long id,
        Long userId,
        String userName,
        Long courseId,
        String courseName,
        List<Long> participantIds,
        Instant createAt,
        Instant updateAt,
        Boolean isActive
) {

    public static CourseClassReturn from(CourseClass courseClass) {
        List<Long> participantIds = List.of();
        if (courseClass.getParticipants() != null) {
            participantIds = courseClass.getParticipants().stream()
                    .map(Participant::getId)
                    .toList();
        }

        Long userId = null;
        String userName = null;
        if (courseClass.getInstructor() != null
                && courseClass.getInstructor().getUser() != null) {
            userId = courseClass.getInstructor().getUser().getId();
            userName = courseClass.getInstructor().getUser().getName();
        }

        Long courseId = null;
        String courseName = null;
        if (courseClass.getCourse() != null) {
            courseId = courseClass.getCourse().getId();
            courseName = courseClass.getCourse().getName();
        }

        return new CourseClassReturn(
                courseClass.getId(),
                userId,
                userName,
                courseId,
                courseName,
                participantIds,
                courseClass.getCreateAt(),
                courseClass.getUpdateAt(),
                courseClass.isActive()
        );
    }
}
