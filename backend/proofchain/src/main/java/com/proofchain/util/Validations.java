package com.proofchain.util;

import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.Course;
import com.proofchain.identities.Instituition;
import com.proofchain.identities.User;
import com.proofchain.repository.CourseRepository;
import com.proofchain.repository.InstituitionRepository;
import com.proofchain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class Validations {

    private final InstituitionRepository instituitionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;


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

    public Optional<User> validateUserExist(String email, Long idInstituition){
        Optional<User> userOptional = userRepository.findByEmailAndInstituition(email, idInstituition) ;
        if(userOptional.isPresent()){
            throw new BusinessRuleException("Usuário já cadastrado");
        }
        return userOptional;
    }

    public Optional<User> validateUserNotExist(String email, Long idInstituition) {
        Optional<User> userOptional = userRepository.findByEmailAndInstituition(email, idInstituition);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não cadastrado");
        }
        return userOptional;
    }

}
