package com.proofchain.business.couseClass.application.handler;

import com.proofchain.business.couseClass.domain.model.CourseClass;
import com.proofchain.admin.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.couseClass.domain.exceptions.CourseClassNotFoundException;
import com.proofchain.business.couseClass.infraestructure.repository.CourseClassRepository;
import com.proofchain.business.couseClass.interfaces.dto.response.CourseClassReturn;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListAllCourseClassHandler {

    private final CourseClassRepository courseClassRepository;
    private final InstitutionRepository institutionRepository;

    public List<CourseClassReturn> listAllcourseClass(){

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

        List<CourseClass> courseClass = courseClassRepository.findAllByInstitution_IdAndInstitution_DeletedAtIsNullOrderByUser_NameAscCourse_NameAsc(institutionId);
        if(courseClass.isEmpty()){
            throw new CourseClassNotFoundException("Não há nenhuma turma cadastrada.");
        }
        return courseClass
                .stream()
                .map(CourseClassReturn::from)
                .toList();
    }
}
