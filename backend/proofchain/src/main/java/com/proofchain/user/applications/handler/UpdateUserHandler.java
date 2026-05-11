package com.proofchain.user.applications.handler;

import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.applications.command.UpdateUserCommand;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.Instant.now;

@Component
@AllArgsConstructor
public class UpdateUserHandler {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    public UserReturn updateUser(Long id, UpdateUserCommand command){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        boolean existInstitution = institutionRepository.existsById(institutionId);
        if (!existInstitution){
            throw new InstitutionNotFoundException();
        }

        boolean existUser = userRepository.existsByIdAndInstitutionId(id, institutionId);
        if (!existUser){
            throw new UserNotFoundException();
        }

        User user = User.update(
            id,
            command.getName(),
            command.getEmail(),
            command.getRole(),
            command.isActive()
            );
        user.setUpdateAt(now());
        user = userRepository.save(user);

        return new UserReturn(user);
    }

}
