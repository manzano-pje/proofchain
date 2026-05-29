package com.proofchain.course.application.handler;

import com.proofchain.course.application.command.CreateCourseCommand;
import com.proofchain.course.domain.exception.CourseIsRegisteredException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.Instant.now;

@Component
@AllArgsConstructor
public class CreateCourseHandler {

    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;

    public void handle(CreateCourseCommand command) {
        Long institutionId = SecurityUtils.getInstitutionId();
        assert institutionId != null;

        Institution institution = institutionRepository.findByIdAndDeletedAtIsNull(institutionId)
                .orElseThrow(InstitutionNotFoundException::new);

        boolean exist = courseRepository.existsByIdAndInstitutionId(command.getId(), institution.getId());
        if (exist) {
            throw new CourseIsRegisteredException();
        }

        Course course = Course.create(
                command.getName(),
                command.getDescription(),
                command.getHours(),
                institution
        );
        courseRepository.save(course);
    }
}