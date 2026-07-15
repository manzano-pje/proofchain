package com.proofchain.instructor.application.handler;

import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.instructor.domain.exceptions.InstructorNotFoundException;
import com.proofchain.instructor.domain.model.Instructor;
import com.proofchain.instructor.infraestructure.repository.InstructorRepository;
import com.proofchain.instructor.interfaces.dto.response.InstructorReturn;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ListAllInstructorHandler {

    private final InstructorRepository instructorRepository;
    private final InstitutionRepository institutionRepository;

    public List<InstructorReturn> listAllInstructor(){

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

        List<Instructor> instructor = instructorRepository.findAllByInstitution_DeletedAtIsNullOrderBy_User_NameAscCourse_NameAsc(institutionId);
        if(instructor.isEmpty()){
            throw new InstructorNotFoundException("Não há nenhum instrutor cadastrado.");
        }
        return instructor
                .stream()
                .map(InstructorReturn::from)
                .toList();
    }
}
