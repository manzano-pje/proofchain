package com.proofchain.admin.institution.application.handler;

import com.proofchain.admin.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.institution.interfaces.dtos.request.UpdateInstitutionRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateInstitutionHandler {

    private final InstitutionRepository institutionRepository;

    public void updateinstitution(String cnpj, UpdateInstitutionRequest UpdateInstitutionRequest){

//        Long institutionId = SecurityUtils.getInstitutionId();
//        validations.validateinstitution(institutionId);

        Institution institution = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj)
                .orElseThrow(() -> new InstitutionNotFoundException());

        institution.updateFrom(UpdateInstitutionRequest);
        institutionRepository.save(institution);
    }
}
