package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.exception.InstitutionNotAutorizedException;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ListOneUserHandler {

    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserReturn listOneUser(String email) {
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        institutionId = 1l;

        if (institutionId == null){
            throw new InstitutionNotAutorizedException();
        }
        boolean existInstitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
                if(!existInstitution){
                    throw new InstitutionNotFoundException();
                }

        // Valida se usuário já existe
        User user = userRepository.findByEmailAndInstitution_Id(email, institutionId)
                .orElseThrow(UserNotFoundException::new) ;
        return new UserReturn(user);
    }
}
