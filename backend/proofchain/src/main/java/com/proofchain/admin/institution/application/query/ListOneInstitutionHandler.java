package com.proofchain.admin.institution.application.query;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.institution.interfaces.dtos.response.InstitutionResponse;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenatValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ListOneInstitutionHandler {

    private final InstitutionRepository institutionRepository;
    private final TenatValidation tenatValidation;

    public InstitutionResponse getOneinstitution(String cnpj){

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);



        Optional<Institution> institutionOptional = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj);
        if(institutionOptional.isEmpty()){
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }
        return InstitutionResponse.from(institutionOptional.get());
    }
}
