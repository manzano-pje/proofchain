package com.proofchain.course.application.handler;

import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.course.interfaces.dto.response.FullCourseResponse;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.institution.Instituition;
import com.proofchain.institution.Institution;
import com.proofchain.security.SecurityUtils;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ListAllCourseHandler {

    private Validations validations;
    private CourseRepository courseRepository;


    public List<FullCourseResponse> listAllCourses(){
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = validations.validateinstitution(institutionId);

        List<Course> courseList = courseRepository.findAll();
        if(courseList.isEmpty()){
            throw new ResourceNotFoundException("Não existem cursos cadastrados.");
        }
        return courseList.stream()
                .map(FullCourseResponse::new)
                .collect(Collectors
                        .toList());
    }
}
