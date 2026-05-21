package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.exception.InstitutionNotAutorizedException;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ListAllUserHandler {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    public List<UserReturn> listAllUser(){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();

        if (institutionId == null){
            throw new InstitutionNotAutorizedException();
        }
        boolean existInstitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if (!existInstitution) {
            throw new InstitutionNotFoundException();
        }
        return userRepository.findAllByInstitution_IdAndInstitution_DeletedAtIsNull(institutionId)
                .stream()
                .map(UserReturn::new)
                .collect(Collectors.toList());
    }
}
