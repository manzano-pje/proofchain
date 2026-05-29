package com.proofchain.institution.application.handler;

import com.proofchain.shared.exception.ResourceNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada."));
        institution.setDeletedAt(Instant.now());

    }
}
