package com.proofchain.user.application;

import com.proofchain.identities.enums.UserRole;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.applications.command.CreateUserCommand;
import com.proofchain.user.applications.handler.CreateUserHandler;
import com.proofchain.user.domain.exception.UserRegisteredException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserHandlerTest {

    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserHandler handler;

    private CreateUserCommand command;
    private Institution institution;
    private User user;

    @BeforeEach
    public void setup() {
        institution = new Institution();
        institution.setId(1L);

        command = new CreateUserCommand(
                "Paulo Manzano",
                "paulomanzano@proofchain.com.br",
                "123456789",
                UserRole.ADMIN
        );

    }

    @Test
    public void ShouldCreateUSerWhenSucessfuly(){

        try( MockedStatic<SecurityUtils> security =
             mockStatic(com.proofchain.security.SecurityUtils.class)) {

            security.when(com.proofchain.security.SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(institution));

            when(userRepository.existsByNameAndInstitutionId(command.getName(), 1L))
                    .thenReturn(false);

            when(passwordEncoder.encode(any()))
                    .thenReturn("Senha encodada");

            handler.createUser(command);

            verify(userRepository, times(1))
                    .save(any(User.class));
        }
    }

    @Test
    public void ShouldCreateUserWhenUserAlerdyExist(){
        try( MockedStatic<SecurityUtils> security =
                     mockStatic(com.proofchain.security.SecurityUtils.class)) {

            security.when(com.proofchain.security.SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(institution));

            when(userRepository.existsByNameAndInstitutionId(command.getName(),1L))
                    .thenReturn(true);


            assertThrows(UserRegisteredException.class,
                    ()-> handler.createUser(command));
        }
    }

    @Test
    public void ShouldCreateUserWhenInstitutionNotFound(){
        try( MockedStatic<SecurityUtils> security =
                mockStatic(com.proofchain.security.SecurityUtils.class)) {

            security.when(com.proofchain.security.SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.empty());


            assertThrows(InstitutionNotFoundException.class,
                    () -> handler.createUser(command));
        }
    }
}
