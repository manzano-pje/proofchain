package com.proofchain.couseClass.application.handler;

import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.couseClass.domain.exceptions.InstructorNotFoundException;
import com.proofchain.couseClass.domain.model.Instructor;
import com.proofchain.couseClass.infraestructure.repository.CourseClassRepository;
import com.proofchain.couseClass.interfaces.dto.response.CourseClassReturn;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListAllCourseClassHandler {

    private final CourseClassRepository courseClassRepository;
    private final InstitutionRepository institutionRepository;

    public List<CourseClassReturn> listAllInstructor(){

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

        List<Instructor> instructor = courseClassRepository.findAllByInstitution_DeletedAtIsNullOrderBy_User_NameAscCourse_NameAsc(institutionId);
        if(instructor.isEmpty()){
            throw new InstructorNotFoundException("Não há nenhum instrutor cadastrado.");
        }
        return instructor
                .stream()
                .map(CourseClassReturn::from)
                .toList();
    }
}
