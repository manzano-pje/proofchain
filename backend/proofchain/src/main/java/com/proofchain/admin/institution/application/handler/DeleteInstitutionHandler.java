package com.proofchain.admin.institution.application.handler;

import com.proofchain.admin.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
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
                .orElseThrow(() -> new InstitutionNotFoundException());
        institution.setDeletedAt(Instant.now());
    }
}
