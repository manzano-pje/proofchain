package com.proofchain.business.instructor.application.query;

import com.proofchain.business.instructor.insfrastructure.repository.InstructorRepository;
import com.proofchain.business.instructor.interfaces.dto.response.InstructorResponse;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstructorMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenatValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetOneInstructorHandler {

    private final InstructorRepository instructorRepository;
    private final TenatValidation tenatValidation;

    public InstructorResponse getOneInstructor(Long id) {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);

        var instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(InstructorMessages.INSTRUCTOR_NOT_FOUND));

        return new InstructorResponse(instructor);
    }

}
