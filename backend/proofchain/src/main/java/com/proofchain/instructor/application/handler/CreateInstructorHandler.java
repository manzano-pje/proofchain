package com.proofchain.instructor.application.handler;

import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.instructor.application.command.RequestInstructorCommand;
import com.proofchain.instructor.domain.exceptions.InstructorIsRegisteredException;
import com.proofchain.instructor.domain.model.Instructor;
import com.proofchain.instructor.infraestructure.repository.InstructorRepository;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CreateInstructorHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS (APPLICATION LAYER)
     * =========================================================
     */

    private final InstructorRepository instructorRepository;
    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public void create(RequestInstructorCommand command){

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = institutionRepository
                .findByIdAndDeletedAtIsNull(institutionId)
                .orElseThrow(InstitutionNotFoundException::new);

        /*
         * =========================================================
         * VALIDAÇÃO DE REGRA DE NEGÓCIO
         * =========================================================
         */

        // verificar instrutor cadastrado

        boolean instructorExist = instructorRepository.existsByUser_IdAndCourse_IdAndInstitution_DeletedAtIsNull(command.getIdUser(), command.getIdCourse(), institutionId);
        if(instructorExist){
            throw new InstructorIsRegisteredException("Instrutor já cadastrado para este curso");
        }

        Optional<Course> course = courseRepository.findByIdAndInstitutionDeletedAsIsNull(command.getIdCourse(), institutionId);
        if(course.isEmpty()){
//            throw new CourseNotFoundException("Curso não existe.");
            throw new CourseNotFoundException();
        }

        Optional<User> user = userRepository.findByInstitution_IdAndInstitution_DeletedAtIsNull(command.getIdUser(), institutionId);
        if(user.isEmpty()){
//            throw new UserNotFoundException("Usuário não existe.");
            throw new UserNotFoundException();
        }

        /*
         * =========================================================
         * CRIAÇÃO DA ENTIDADE DE DOMÍNIO
         * =========================================================
         */

        Instructor instructor = new Instructor();

        instructor.setUser(user.get());
        instructor.setCourse(course.get());
        instructor.setCreateAt(Instant.now());
        instructor.setUpdateAt(null);
        instructor.setActive(true);
        instructor.setInstitution(institution);

        /*
         * =========================================================
         * PERSISTÊNCIA
         * =========================================================
         */

        instructorRepository.save(instructor);

    }
}
