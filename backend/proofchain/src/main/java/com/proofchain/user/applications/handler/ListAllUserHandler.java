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

//        Institution institution = institutionRepository.findById(institutionId)
//                .orElseThrow(InstitutionNotFoundException::new);

        List<User> users = userRepository.findAllByUser_IdAndInstitution_DeletedAtIsNull(institutionId);
        if(users.isEmpty()){
            throw new UserNotFoundException();
        }
        return users
                .stream()
                .map(UserReturn::new)
                .collect(Collectors.toList());
    }
}
