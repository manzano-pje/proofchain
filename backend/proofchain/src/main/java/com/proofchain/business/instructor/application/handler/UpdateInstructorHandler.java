package com.proofchain.business.instructor.application.handler;

import com.proofchain.business.instructor.application.command.UpdateInstructorCommand;
import com.proofchain.business.instructor.domain.model.Instructor;
import com.proofchain.business.instructor.insfrastructure.repository.InstructorRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstructorMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenatValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateInstructorHandler {

    private final InstructorRepository instructorRepository;
    private final TenatValidation tenatValidation;

    public void updateInstructor(Long id, UpdateInstructorCommand command) {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);

        /*
         * =========================================================
         * ATUALIZAÇÃO DE INSTRUTOR
         * =========================================================
         */
        var instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(InstructorMessages.INSTRUCTOR_NOT_FOUND));

        Instructor isntructor = new Instructor();
        instructor.setIdInstructor(id);
        instructor.setActive(command.isActive());
        instructor.setSpecialty(command.getSpecialty());
        instructor.setHiringDate(command.getHiringDate());

        instructorRepository.save(instructor);



    }

}
