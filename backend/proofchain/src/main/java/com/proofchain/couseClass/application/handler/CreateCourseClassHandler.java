package com.proofchain.couseClass.application.handler;

import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.couseClass.application.command.RequestCourseClassCommand;
import com.proofchain.couseClass.domain.exceptions.CourseClassInstructorIsRegisteredException;
import com.proofchain.couseClass.domain.model.CourseClass;
import com.proofchain.couseClass.infraestructure.repository.CourseClassRepository;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CreateCourseClassHandler {

    /*
     * =========================================================
     * DEPENDÊNCIAS (APPLICATION LAYER)
     * =========================================================
     */

    private final CourseClassRepository courseClassRepository;
    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public void create(RequestCourseClassCommand command){

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


        boolean instructorExist = courseClassRepository.existsByIdAndCourse_IdAndInstitution_IdAndInstitution_DeletedAtIsNull(command.getIdUser(), command.getIdCourse(), institutionId);
        if(instructorExist){
            throw new CourseClassInstructorIsRegisteredException("Instrutor já cadastrado para este curso");
        }

        Optional<Course> course = courseRepository.findByIdAndInstitutionId(command.getIdCourse(), institutionId);
        if(course.isEmpty()){
//            throw new CourseNotFoundException("Curso não existe.");
            throw new CourseNotFoundException();
        }

        Optional<User> user = userRepository.findByIdAndInstitution_Id(command.getIdUser(), institutionId);
        if(user.isEmpty()){
//            throw new UserNotFoundException("Usuário não existe.");
            throw new UserNotFoundException();
        }

        /*
         * =========================================================
         * CRIAÇÃO DA ENTIDADE DE DOMÍNIO
         * =========================================================
         */

        CourseClass courseClass = new CourseClass();

        courseClass.setUser(user.get());
        courseClass.setCourse(course.get());
        courseClass.setCreateAt(Instant.now());
        courseClass.setUpdateAt(null);
        courseClass.setActive(true);
        courseClass.setInstitution(institution);

        /*
         * =========================================================
         * PERSISTÊNCIA
         * =========================================================
         */

        courseClassRepository.save(courseClass);

    }
}
