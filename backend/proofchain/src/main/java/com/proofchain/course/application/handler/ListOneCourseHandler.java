package com.proofchain.course.application.handler;

import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.course.interfaces.dto.response.CourseResponse;
import com.proofchain.instituition.Instituition;
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

    public CourseResponse listOneCourse(String name){
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = validations.validateinstitution(institutionId);
        Optional<Course> courseOptional = courseRepository.findByNameAndInstitutionId(name, SecurityUtils.getInstituitionId());
        if(courseOptional.isEmpty()){
            throw new CourseNotFoundException();
        }
        return new CourseResponse(courseOptional.get());
    }
}
