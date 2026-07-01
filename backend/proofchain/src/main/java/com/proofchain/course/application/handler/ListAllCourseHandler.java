package com.proofchain.course.application.handler;

import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.course.interfaces.dto.response.FullCourseResponse;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ListAllCourseHandler {

    private CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;

    public List<FullCourseResponse> listAllCourses(){
//        Long institutionId = SecurityUtils.getInstitutionId();
        Long institutionId = 1L;
        boolean existInstitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if(!existInstitution){
            throw new InstitutionNotFoundException();
        }

        List<Course> courseList = courseRepository.findAllByInstitutionId(institutionId);
        if(courseList.isEmpty()){
            throw new CourseNotFoundException();
        }
        return courseList.stream()
                .map(FullCourseResponse::new)
                .collect(Collectors
                        .toList());
    }
}
