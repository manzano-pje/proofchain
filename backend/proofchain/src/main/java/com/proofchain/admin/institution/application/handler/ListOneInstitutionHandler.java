package com.proofchain.admin.institution.application.handler;

import com.proofchain.admin.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.institution.interfaces.dtos.response.InstitutionReturn;
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
            throw new InstitutionNotFoundException();
        }
        return InstitutionReturn.from(institutionOptional.get());
    }
}
