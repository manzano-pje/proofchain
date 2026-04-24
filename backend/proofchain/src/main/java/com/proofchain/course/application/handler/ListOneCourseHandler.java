package com.proofchain.course.application.handler;

import com.proofchain.course.domain.model.Course;
import com.proofchain.course.interfaces.dto.response.CourseResponse;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.instituition.Institution;
import com.proofchain.security.SecurityUtils;

import java.util.Optional;

public class ListOneCourseHandler {
    public CourseResponse listOneCourse(String name){
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = validations.validateinstitution(institutionId);

        Optional<Course> courseOptional = courseRepository.findByNameAndInstitutionId(name, SecurityUtils.getInstitutionId());
        if(courseOptional.isEmpty()){
            throw new ResourceNotFoundException("Curso não encontrado.");
        }
        return new CourseResponse(courseOptional.get());
    }
}
