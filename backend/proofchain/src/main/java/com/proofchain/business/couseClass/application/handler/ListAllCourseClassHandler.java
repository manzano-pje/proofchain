package com.proofchain.business.couseClass.application.handler;

import com.proofchain.business.couseClass.domain.model.CourseClass;
import com.proofchain.business.couseClass.infraestructure.repository.CourseClassRepository;
import com.proofchain.business.couseClass.interfaces.dto.response.CourseClassReturn;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.CourseMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListAllCourseClassHandler {

    private final CourseClassRepository courseClassRepository;
    private final TenantValidation tenantValidation;

    @Transactional
    public List<CourseClassReturn> listAllcourseClass(){

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);

        /*
         * =========================================================
         * VALIDAÇÃO DE REGRA DE NEGÓCIO
         * =========================================================
         */

        List<CourseClass> courseClasses = courseClassRepository.findAllByInstitution_IdAndInstitution_DeletedAtIsNullOrderByUser_NameAscCourse_NameAsc(institutionId);
        if(courseClasses.isEmpty()){
            throw new NotFoundException(CourseMessages.COURSE_NOT_FOUND);
        }

        return courseClasses.stream()
                .map(CourseClassReturn::from)
                .toList();
    }
}
