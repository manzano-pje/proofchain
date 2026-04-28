package com.proofchain.util;

import com.proofchain.course.domain.exception.BusinessRuleException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.instituition.Institution;
import com.proofchain.instituition.InstitutionRepository;
import com.proofchain.user.User;
import com.proofchain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class Validations {

    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;


    // 🔑 Instituição vem do TOKEN, não do request
    public Institution validateinstitution(Long institutionId) {;
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada"));
        return institution;
    }

    public Optional<Course>validateCourseExist(String name, Long idinstitution){
        Optional<Course> courseOptional = courseRepository.findByNameAndInstitutionId(name,idinstitution);
        if(courseOptional.isPresent()){
            throw new BusinessRuleException("Curso já cadatrado.");
        }
        return courseOptional;
    }

    public Optional<Course>validateCourseNoExist(String name, Long idinstitution){
        Optional<Course> courseOptional = courseRepository.findByNameAndInstitutionId(name,idinstitution);
        if(courseOptional.isEmpty()){
            throw new ResourceNotFoundException("Este curso não está cadatrado.");
        }
        return courseOptional;
    }

    public Optional<User> validateUserExist(String email, Long idinstitution){
        Optional<User> userOptional = userRepository.findByNameAndInstitutionId(email, idinstitution) ;
        if(userOptional.isPresent()){
            throw new BusinessRuleException("Usuário já cadastrado");
        }
        return userOptional;
    }

    public Optional<User> validateUserNotExist(String email, Long idinstitution) {
        Optional<User> userOptional = userRepository.findByNameAndInstitutionId(email, idinstitution);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não cadastrado");
        }
        return userOptional;
    }

}
