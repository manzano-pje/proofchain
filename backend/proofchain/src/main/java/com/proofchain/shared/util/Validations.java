package com.proofchain.shared.util;

import com.proofchain.course.domain.exception.CourseIsRegisteredException;
import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.user.infrastructure.repository.UserRepository;
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
    public Institution validateinstitution(Long institutionId) {
        return institutionRepository.findById(institutionId)
                .orElseThrow(() -> new NotFoundException("Instituição não encontrada"));
    }

    public Optional<Course>validateCourseExist(Long id, Long idinstitution){
        Optional<Course> courseOptional = courseRepository.findByIdAndInstitutionId(id,idinstitution);
        if(courseOptional.isPresent()){
            throw new CourseIsRegisteredException();
        }
        return courseOptional;
    }

    public Optional<Course>validateCourseNoExist(Long id, Long idinstitution){
        Optional<Course> courseOptional = courseRepository.findByIdAndInstitutionId(id,idinstitution);
        if(courseOptional.isEmpty()){
            throw new CourseNotFoundException();
        }
        return courseOptional;
    }


//    public Optional<User> validateUserNotExist(String email, Long idinstitution) {
//        Optional<User> userOptional = userRepository.findByNameAndInstitutionId(email, idinstitution);
//        if (userOptional.isEmpty()) {
//            throw new ResourceNotFoundException("Usuário não cadastrado");
//        }
//        return userOptional;
//    }

}
