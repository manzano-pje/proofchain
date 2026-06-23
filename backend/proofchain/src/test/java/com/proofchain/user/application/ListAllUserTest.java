package com.proofchain.user.application;

import com.proofchain.user.domain.model.UserRole;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.user.applications.handler.ListAllUserHandler;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListAllUserTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private InstitutionRepository institutionRepository;
    @InjectMocks
    private ListAllUserHandler handler;

    private Institution institution;
    private User user1;
    private User user2;

    @BeforeEach
    public void setup(){

        institution = new Institution();
        institution.setId(1L);

        user1 = new User(
                1L,
                "Paulo Manzano",
                "paulomanzano@proofchain.com.br",
                "123456789",
                UserRole.ADMIN,
                Instant.now(),
                null,
                true,
                institution
        );
        user2 = new User(
                2L,
                "Andréa Manzano",
                "andreamanzano@proofchain.com.br",
                "123456789",
                UserRole.USER,
                Instant.now(),
                null,
                true,
                institution
        );
    }

    @Test
    public void ShouldListAllUserWhenSuccessfuly(){

        try(MockedStatic<SecurityUtils> security =
                mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            List<User> users =
                    List.of(user1, user2);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(true);

            when(userRepository.findAllByInstitution_IdAndInstitution_DeletedAtIsNull(1L))
                    .thenReturn(users);

            handler.listAllUser();

            verify(userRepository, times(1))
                    .findAllByInstitution_IdAndInstitution_DeletedAtIsNull(1L);
        }
    }

    @Test
    public void ShouldListAllUserWhenNotFound(){
        try(MockedStatic<SecurityUtils> security =
                mockStatic(SecurityUtils.class)){

            security.when(SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(true);

            when(userRepository.findAllByInstitution_IdAndInstitution_DeletedAtIsNull(1L))
                    .thenReturn(Collections.emptyList());

            assertThrows(UserNotFoundException.class,
            ()-> handler.listAllUser());
        }
    }

    @Test
    public void ShouldListAllUserWhenInstitutionNotFound(){
        try(MockedStatic<SecurityUtils> security =
                    mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(false);

            assertThrows(InstitutionNotFoundException.class,
                    () -> handler.listAllUser());
        }
    }
}
