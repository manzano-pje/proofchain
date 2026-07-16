package com.proofchain.admin.institution.application.handler;

import com.proofchain.admin.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.institution.interfaces.dtos.response.InstitutionReturn;
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
            throw new InstitutionNotFoundException();
        }
        return institutionList.stream()
                .map(InstitutionReturn::from)
                .collect(Collectors.toList());
    }
}
