package com.proofchain.user.applications.handler;

import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ListAllUserHandler {

    private final UserRepository userRepository;
    private final Validations validations;

    public List<UserReturn> listAllUser(){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = validations.validateinstitution(institutionId);

        List<User> userList = userRepository.findAll();
        if(userList.isEmpty()){
            throw new ResourceNotFoundException("Não há usuários cadsatrados.");
        }

        return userList.stream()
                .map(UserReturn::new)
                .collect(Collectors.toList());
    }
}
