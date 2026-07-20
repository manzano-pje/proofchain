package com.proofchain.admin.institution.application.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@AllArgsConstructor
public class DeleteInstitutionHandler {

    private InstitutionRepository institutionRepository;

    @Transactional
    public void deleteinstitution(String cnpj){

//        Long institutionId = SecurityUtils.getInstitutionId();
//        validations.validateinstitution(institutionId);

        Institution institution = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj)
                .orElseThrow(() -> new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND));
        institution.setDeletedAt(Instant.now());
    }
}
