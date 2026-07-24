package com.proofchain.business.instructor.application.handler;

import com.proofchain.business.instructor.insfrastructure.repository.InstructorRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstructorMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenatValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeleteInstructorHandler {

    private final InstructorRepository instructorRepository;
    private final TenatValidation tenatValidation;

    public void deleteInstructor(Long id) {
        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);

        var existingInstructor = instructorRepository.existsByIdInstructorAndIsActiveIsTrue(id);
        if (!existingInstructor) {
            throw new NotFoundException(InstructorMessages.INSTRUCTOR_NOT_FOUND);
        }

        instructorRepository.deleteById(id);
    }




}
