package com.proofchain.institution.application.handler;

import com.proofchain.shared.exception.ResourceNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.institution.interfaces.dtos.response.InstitutionReturn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ListAllInstitutionHandler {

    private final InstitutionRepository institutionRepository;

    public List<InstitutionReturn> getAllinstitution(){

        List<Institution> institutionList = institutionRepository.findAllByDeletedAtIsNull();
        if(institutionList.isEmpty()){
            throw new ResourceNotFoundException("Não existem instituições cadastradas.");
        }
        return institutionList.stream()
                .map(InstitutionReturn::from)
                .collect(Collectors.toList());
    }
}
