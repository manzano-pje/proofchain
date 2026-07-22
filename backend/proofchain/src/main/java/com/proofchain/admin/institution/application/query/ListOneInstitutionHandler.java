package com.proofchain.admin.institution.application.query;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.institution.interfaces.dtos.response.InstitutionResponse;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ListOneInstitutionHandler {

    private final InstitutionRepository institutionRepository;

    public InstitutionResponse getOneinstitution(String cnpj){

        Long institutionId = SecurityUtils.getInstitutionId();
        assert institutionId != null;


        Optional<Institution> institutionOptional = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj);
        if(institutionOptional.isEmpty()){
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }
        return InstitutionResponse.from(institutionOptional.get());
    }
}
