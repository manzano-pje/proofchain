package com.proofchain.util;

import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.Course;
import com.proofchain.identities.Instituition;
import com.proofchain.repository.CourseRepository;
import com.proofchain.repository.InstituitionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class Validations {

    private InstituitionRepository instituitionRepository;
    private CourseRepository courseRepository;
    // 🔑 Instituição vem do TOKEN, não do request

    public Instituition validateInstituition(Long institutionId) {;
        Instituition institution = instituitionRepository.findByidInstituition(institutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada"));
        return institution;
    }

    public Optional<Course>validateCourseExist(String name, Long idInstituition){
        Optional<Course> courseOptional = courseRepository.findByNameAndInstituition(name,idInstituition);
        if(courseOptional.isPresent()){
            throw new BusinessRuleException("Curso já cadatrado.");
        }
        return courseOptional;
    }

    public Optional<Course>validateCourseNoExist(String name, Long idInstituition){
        Optional<Course> courseOptional = courseRepository.findByNameAndInstituition(name,idInstituition);
        if(courseOptional.isEmpty()){
            throw new ResourceNotFoundException("Este curso não está cadatrado.");
        }
        return courseOptional;
    }

}
