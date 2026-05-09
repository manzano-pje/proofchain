package com.proofchain.institution.application.handler;

import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.institution.interfaces.dtos.response.InstitutionReturn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ListOneInstitutionHandler {

    private final InstitutionRepository institutionRepository;

    public InstitutionReturn getOneinstitution(String cnpj){

//        Long institutionId = SecurityUtils.getInstitutionId();
//        validations.validateinstitution(institutionId);

        Optional<Institution> institutionOptional = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj);
        if(institutionOptional.isEmpty()){
            throw new ResourceNotFoundException("Instituição não encontrada.");
        }
        return InstitutionReturn.from(institutionOptional.get());
    }
}
