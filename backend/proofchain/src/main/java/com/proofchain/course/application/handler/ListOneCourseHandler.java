package com.proofchain.course.application.handler;

import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.course.interfaces.dto.response.CourseResponse;
import com.proofchain.institution.Institution;
import com.proofchain.security.SecurityUtils;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ListOneCourseHandler {

    private final Validations validations;
    private final CourseRepository courseRepository;

    public CourseResponse listOneCourse(Long id){
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = validations.validateinstitution(institutionId);
        Optional<Course> courseOptional = courseRepository.findByIdAndInstitutionId(id, SecurityUtils.getInstitutionId());
        if(courseOptional.isEmpty()){
            throw new CourseNotFoundException();
        }
        return new CourseResponse(courseOptional.get());
    }
}
