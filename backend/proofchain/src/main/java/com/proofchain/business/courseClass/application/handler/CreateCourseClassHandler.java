package com.proofchain.business.courseClass.application.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.business.courseClass.application.command.RequestCourseClassCommand;
import com.proofchain.business.courseClass.domain.model.CourseClass;
import com.proofchain.business.courseClass.infrastructure.repository.CourseClassRepository;
import com.proofchain.business.instructor.domain.model.Instructor;
import com.proofchain.business.instructor.insfrastructure.repository.InstructorRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.CourseMessages;
import com.proofchain.shared.exception.messages.InstructorMessages;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
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
    private final InstructorRepository instructorRepository;
    private final TenantValidation tenantValidation;

    public void create(RequestCourseClassCommand command){

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);

        /*
         * =========================================================
         * BUSCA DE TURMA
         * =========================================================
         */
        boolean existsClourseClass = courseClassRepository.existsByCourse_idAndInstitution_IdAndInstitution_DeletedAtIsNullAndIsActiveTrue(
                command.getIdCourse(),
                institutionId );

        if(existsClourseClass){
            throw new AlreadyExistsException(CourseMessages.COURSE_ALREADY_EXISTS);
        }

        /*
         * =========================================================
         * BUSCA DE INSTITUIÇÃO
         * =========================================================
         */
        Optional<Institution> institution = institutionRepository.findByIdAndDeletedAtIsNull(institutionId);

        /*
         * =========================================================
         * VALIDAÇÃO DE INSTRUTOR NA TURMA
         * =========================================================
         */
        boolean instructorExist = courseClassRepository.existsByIdAndCourse_IdAndInstitution_IdAndInstitution_DeletedAtIsNull(command.getIdUser(), command.getIdCourse(), institutionId);
        if(!instructorExist){
            throw new NotFoundException(InstructorMessages.INSTRUCTOR_ALREAY_EXISTS);
        }

        /*
         * =========================================================
         * BUSCA DE INSTRUTOR
         * =========================================================
         */
        Optional<Instructor> instructor = instructorRepository.findByIdAndInstitutionDeletedAtIsNull(command.getIdUser(), institutionId);
        if(instructor.isEmpty()){
            throw new NotFoundException(InstructorMessages.INSTRUCTOR_NOT_FOUND);
        }

        /*
         * =========================================================
         * BUSCA DE CURSO
         * =========================================================
         */
        Optional<Course> course = courseRepository.findByIdAndInstitutionId(command.getIdCourse(), institutionId);
        if(course.isEmpty()){
            throw new NotFoundException(CourseMessages.COURSE_NOT_FOUND);
        }

        /*
         * =========================================================
         * CRIAÇÃO DA ENTIDADE DE DOMÍNIO
         * =========================================================
         */

        CourseClass courseClass = new CourseClass();

        courseClass.setInstructor(instructor.get());   // instrutor
        courseClass.setCourse(course.get());           // Curso
        courseClass.setCreateAt(Instant.now());        // Criação da turma
        courseClass.setUpdateAt(null);                 // atualização da turma
        courseClass.setActive(true);                   // se a turma está ativa
        courseClass.setInstitution(institution.get()); // vínculo da turma com instituição

        /*
         * =========================================================
         * PERSISTÊNCIA
         * =========================================================
         */

        courseClassRepository.save(courseClass);

    }
}
