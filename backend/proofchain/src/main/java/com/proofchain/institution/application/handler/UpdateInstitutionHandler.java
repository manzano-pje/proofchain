package com.proofchain.institution.application.handler;

import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.institution.interfaces.dtos.request.InstitutionRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateInstitutionHandler {

    private final InstitutionRepository institutionRepository;

    public void updateinstitution(String cnpj, InstitutionRequest institutionRequest){

//        Long institutionId = SecurityUtils.getInstitutionId();
//        validations.validateinstitution(institutionId);

        Institution institution = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada."));

        institution.updateFrom(institutionRequest);
        institutionRepository.save(institution);
    }
}
