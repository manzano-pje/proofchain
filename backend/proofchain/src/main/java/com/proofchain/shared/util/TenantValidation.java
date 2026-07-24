package com.proofchain.shared.util;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantValidation {

    private final InstitutionRepository institutionRepository;

    public void validateInstitution(Long institutionId) {

        boolean exists = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if(!exists){
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }
    }
}
