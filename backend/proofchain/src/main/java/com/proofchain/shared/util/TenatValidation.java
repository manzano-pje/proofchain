package com.proofchain.shared.util;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenatValidation {

    private InstitutionRepository institutionRepository;

    public void validateInstitution(Long institutionId) {

        boolean exist = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if(!exist){
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }
    }
}
