package com.proofchain.user.applications.handler;

import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.request.UserUpdateDto;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.time.Instant.now;

@Component
@AllArgsConstructor
public class UpdateUserHandler {

    private final Validations validations;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserReturn updateUser(String email, UserUpdateDto userUpadte){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution instituition = validations.validateinstitution(institutionId);

        Optional<User> userOptional = validations.validateUserNotExist(email, institutionId);

        User user = new User();
        user.setId(userOptional.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado.")).getId());
        user.setName(userUpadte.getName());
        user.setRole(userUpadte.getRole());
        user.setActive(userUpadte.isActive());
        user.setUpdateAt(now());
        user = userRepository.save(user);

        return mapper.map(user, UserReturn.class);
    }

}
