package com.proofchain.course.application.handler;

import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.course.interfaces.dto.response.CourseResponse;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Handler for listing a single course by its ID.
 * Author - Paulo Manzano
 * version - 1.0
 * since - 2024-06-01
 */


@Component
@AllArgsConstructor
public class ListOneCourseHandler {

    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;


    /**
     * Lists a single course by its ID.
     * @param id
     * @return CourseResponse
     */

    public CourseResponse listOneCourse(Long id){
        Long institutionId = SecurityUtils.getInstitutionId();
        boolean existInstitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if(!existInstitution) {
            throw new InstitutionNotFoundException();
        }

        Optional<Course> courseOptional = courseRepository.findByIdAndInstitutionId(id, SecurityUtils.getInstitutionId());
        if(courseOptional.isEmpty()){
            throw new CourseNotFoundException();
        }
        return new CourseResponse(courseOptional.get());
    }
}
