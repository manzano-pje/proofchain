package com.proofchain.course.application.handler;

import com.proofchain.course.application.command.UpdateCourseCommand;
import com.proofchain.course.domain.exception.CourseIsRegisteredException;
import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UpdateCourseHandler {

    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;

    public Course updateCourse(Long id, UpdateCourseCommand command) {
        //Long institutionId = SecurityUtils.getInstitutionId();
        Long institutionId = 1L;
        assert institutionId != null;

        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(InstitutionNotFoundException::new);

        Course course = courseRepository.findByIdAndInstitutionId(id, institutionId)
                .orElseThrow(CourseNotFoundException::new);

        boolean exist = courseRepository.existsByIdAndInstitutionId(command.getId(), institution.getId());

        if (exist && !course.getName().equals(command.getName())) {
            throw new CourseIsRegisteredException();
        }

        course.updateCourse(
                command.getName(),
                command.getDescription(),
                command.getHours()
        );
        courseRepository.save(course);
        return course;
    }
}