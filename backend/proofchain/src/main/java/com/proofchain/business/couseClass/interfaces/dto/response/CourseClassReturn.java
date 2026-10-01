package com.proofchain.business.couseClass.interfaces.dto.response;

import com.proofchain.business.couseClass.domain.model.CourseClass;
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
        List<Long> participantIds = courseClass.getParticipants() == null
                ? List.of()
                : courseClass.getParticipants().stream()
                    .map(Participant::getId)
                    .toList();

        return new CourseClassReturn(
                courseClass.getId(),
                courseClass.getUser() != null ? courseClass.getUser().getId() : null,
                courseClass.getUser() != null ? courseClass.getUser().getName() : null,
                courseClass.getCourse() != null ? courseClass.getCourse().getId() : null,
                courseClass.getCourse() != null ? courseClass.getCourse().getName() : null,
                participantIds,
                courseClass.getCreateAt(),
                courseClass.getUpdateAt(),
                courseClass.isActive()
        );
    }
}
