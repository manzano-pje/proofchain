package com.proofchain.course.application.handler;

import com.proofchain.course.application.command.CreateCourseCommand;
import com.proofchain.course.domain.exception.InstrituitionNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.exceptions.ValidationException;
import com.proofchain.instituition.Instituition;
import com.proofchain.instituition.InstituitionRepository;
import com.proofchain.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.Instant.now;

@Component
@AllArgsConstructor
public class CreateCourseHandler {

    private final CourseRepository courseRepository;
    private final InstituitionRepository instituitionRepository;

    public void handle(CreateCourseCommand command) {
        Long institutionId = SecurityUtils.getInstituitionId();
        assert institutionId != null;
        Instituition instituition = instituitionRepository.findById(institutionId)
                .orElseThrow(InstrituitionNotFoundException::new);
        boolean exist = courseRepository.existsByIdCourseAndInstituitionId(command.getId(), instituition.getId());
        if (exist) {
            throw new ValidationException("Curso já cadastrado");
        }

        Course course = Course.create(
                command.getName(),
                command.getDescription(),
                command.getHours(),
                instituition
        );
        course.setCreatedAt(now());
        courseRepository.save(course);
    }
}