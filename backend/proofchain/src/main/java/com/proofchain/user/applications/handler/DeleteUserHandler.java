package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteUserHandler {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    public void deleteUSer(Long id){
        // 🔑 Instituição vem do TOKEN, não do request
//        Long institutionId = SecurityUtils.getInstitutionId();
        Long institutionId = 1L;

        boolean existInstitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if (!existInstitution){
            throw new InstitutionNotFoundException();
        }

        boolean existUser = userRepository.existsByIdAndInstitutionId(id, institutionId);
        if (!existUser){
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }
}
