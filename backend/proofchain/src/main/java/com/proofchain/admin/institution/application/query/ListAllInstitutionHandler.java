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

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListAllInstitutionHandler {

    private final InstitutionRepository institutionRepository;
    private final TenatValidation tenatValidation;

    public List<InstitutionResponse> getAllinstitution(){

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);


        List<Institution> institutionList = institutionRepository.findAllByDeletedAtIsNull();
        if(institutionList.isEmpty()){
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }
        return institutionList.stream()
                .map(InstitutionResponse::from)
                .collect(Collectors.toList());
    }
}
