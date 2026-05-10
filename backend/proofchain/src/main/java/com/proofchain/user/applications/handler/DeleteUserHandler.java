package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class DeleteUserHandler {

    private final UserRepository userRepository;
    private final Validations validations;

    public void deleteUSer(Long id){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        boolean existInstitution = userRepository.existsById(institutionId);
        if (!existInstitution){
            throw new InstitutionNotFoundException();
        }

        boolean existUser = userRepository.existByIdAndInstitutionId(id, institutionId);
        if (!existUser){
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }
}
