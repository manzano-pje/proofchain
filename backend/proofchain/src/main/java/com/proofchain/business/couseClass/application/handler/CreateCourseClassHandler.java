package com.proofchain.business.couseClass.application.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.business.couseClass.application.command.RequestCourseClassCommand;
import com.proofchain.business.couseClass.domain.model.CourseClass;
import com.proofchain.business.couseClass.infraestructure.repository.CourseClassRepository;
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
    private final UserRepository userRepository;
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
         * BUSCA DE INSTITUIÇÃO
         * =========================================================
         */
        Optional<Institution> institution = institutionRepository.findByIdAndDeletedAtIsNull(institutionId);

        /*
         * TODO CORRIGIR VALIDAÇÃO INSTRUTOR PARA ENTIDADE INSTRUTOR
         * =========================================================
         * VALIDAÇÃO DE INSTRUTOR
         * =========================================================
         */
        boolean instructorExist = courseClassRepository.existsByIdAndCourse_IdAndInstitution_IdAndInstitution_DeletedAtIsNull(command.getIdUser(), command.getIdCourse(), institutionId);
        if(instructorExist){
            throw new AlreadyExistsException(InstructorMessages.INSTRUCTOR_ALREAY_EXISTS);
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
         * BUSCA DE USUÁRIO
         * =========================================================
         */
        Optional<User> user = userRepository.findByIdAndInstitution_Id(command.getIdUser(), institutionId);
        if(user.isEmpty()){
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
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
        courseClass.setInstitution(institution.get());

        /*
         * =========================================================
         * PERSISTÊNCIA
         * =========================================================
         */

        courseClassRepository.save(courseClass);

    }
}
