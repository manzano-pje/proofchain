package com.proofchain.course.application.handler;

import com.proofchain.course.domain.exception.CourseIsRegisteredException;
import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.course.interfaces.dto.response.CourseResponse;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.security.SecurityUtils;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ListOneCourseHandler {

    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;

    public CourseResponse listOneCourse(Long id){
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(InstitutionNotFoundException::new);

        Optional<Course> courseOptional = courseRepository.findByIdAndInstitutionIdAndDeletedAtIsNull(id, SecurityUtils.getInstitutionId());
        if(courseOptional.isEmpty()){
            throw new CourseNotFoundException();
        }
        return new CourseResponse(courseOptional.get());
    }
}
