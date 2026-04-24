package com.proofchain.course.application.handler;

import com.proofchain.course.application.command.CreateCourseCommand;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.exceptions.ValidationException;
import com.proofchain.instituition.Institution;
import com.proofchain.instituition.InstitutionRepository;
import com.proofchain.security.SecurityUtils;
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
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada"));

        boolean exist = courseRepository.existsByNameAndInstitutionId(command.getName(), institution.getId());
        if (exist) {
            throw new ValidationException("Curso já cadastrado");
        }

        Course course = Course.create(
                command.getName(),
                command.getDescription(),
                command.getHours(),
                institution
        );
        course.setCreatedAt(now());

        courseRepository.save(course);
    }
}