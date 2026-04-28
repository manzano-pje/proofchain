package com.proofchain.course.application.handler;

import com.proofchain.course.application.command.UpdateCourseCommand;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.exceptions.ValidationException;
import com.proofchain.instituition.Institution;
import com.proofchain.instituition.InstitutionRepository;
import com.proofchain.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@AllArgsConstructor
public class UpdateCourseHandler {

    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;

    public Course updateCourse(Long id, UpdateCourseCommand command) {
        Long institutionId = SecurityUtils.getInstitutionId();
        assert institutionId != null;

        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada"));

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));

        boolean exist = courseRepository.existsByNameAndInstitutionId(command.getName(), institution.getId());

        if (exist && !course.getName().equals(command.getName())) {
            throw new ValidationException("Curso já cadastrado");
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
