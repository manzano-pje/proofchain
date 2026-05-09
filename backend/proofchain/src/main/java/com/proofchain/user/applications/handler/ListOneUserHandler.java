package com.proofchain.user.applications.handler;

import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.plataform.domain.ModelMapperConfig;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ListOneUserHandler {

    private final Validations validations;
    private final ModelMapperConfig mapper;

    public UserReturn listOneUser(String email) {
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = validations.validateinstitution(institutionId);;

        // Valida se usuário não existe
        Optional<User> userOptional = validations.validateUserNotExist(email, institutionId);

        UserReturn user = mapper.modelMapper()
                .map(userOptional.orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado.")), UserReturn.class);
        return user;
    }
}
