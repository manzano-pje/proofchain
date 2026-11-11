package com.proofchain.business.instructor.application.query;

import com.proofchain.business.instructor.domain.model.Instructor;
import com.proofchain.business.instructor.insfrastructure.repository.InstructorRepository;
import com.proofchain.business.instructor.interfaces.dto.response.InstructorsSumaryResponse;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListAllInstructorHandler {

    private final TenantValidation tenantValidation;
    private final InstructorRepository instructorRepository;

    public List<InstructorsSumaryResponse> listAllInstructors() {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);

        List<Instructor> instructors =
                instructorRepository.findAllByInstitution_IdAndIsActiveTrue(institutionId);
        List<InstructorsSumaryResponse> insntructorResponse = instructors
                .stream()
                .map(InstructorsSumaryResponse::new)
                .toList();

        return insntructorResponse;
    }
}
