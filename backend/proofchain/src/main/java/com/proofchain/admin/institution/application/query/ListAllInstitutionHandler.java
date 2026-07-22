package com.proofchain.admin.institution.application.query;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.institution.interfaces.dtos.response.InstitutionResponse;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ListAllInstitutionHandler {

    private final InstitutionRepository institutionRepository;

    public List<InstitutionResponse> getAllinstitution(){

        //TODO criar validação por token

        List<Institution> institutionList = institutionRepository.findAllByDeletedAtIsNull();
        if(institutionList.isEmpty()){
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }
        return institutionList.stream()
                .map(InstitutionResponse::from)
                .collect(Collectors.toList());
    }
}
