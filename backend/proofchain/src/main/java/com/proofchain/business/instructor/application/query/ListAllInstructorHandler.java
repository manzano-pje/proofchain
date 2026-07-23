package com.proofchain.business.instructor.application.query;

import com.proofchain.business.instructor.domain.model.Instructor;
import com.proofchain.business.instructor.insfrastructure.repository.InstructorRepository;
import com.proofchain.business.instructor.interfaces.dto.response.InstructorsSumaryResponse;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstructorMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenatValidation;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListAllInstructorHandler {

    private final TenatValidation tenatValidation;
    private final InstructorRepository instructorRepository;;
    private final UserRepository userRepository;

    public List<InstructorsSumaryResponse> listAllInstructors() {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);

        List<Instructor> instructors = instructorRepository.findAllByIsActiveIsTrue();
        if(instructors.isEmpty()) {
            throw new NotFoundException(InstructorMessages.INSTRUCTOR_NOT_FOUND);
        }
        List<InstructorsSumaryResponse> insntructorResponse = instructors
                .stream()
                .map(InstructorsSumaryResponse::new)
                .toList();

        return insntructorResponse; // Replace with actual implementation
    }
}
