package com.proofchain.user.unitTest;

/**
 * Testes unitários do caso de uso de atualização de usuários.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a atualização dos dados permitidos, o isolamento
 * entre instituições e o tratamento de usuário inexistente.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.applications.command.UpdateUserCommand;
import com.proofchain.user.applications.handler.UpdateUserHandler;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.domain.model.UserRole;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UNI_02_UpdateUserHandlerTest {

    @Mock private UserRepository userRepository;
    @Mock private InstitutionRepository institutionRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private UpdateUserHandler updateUserHandler;

    @Test
    @DisplayName("UNI_02.01 - Should update authenticated user data")
    void shouldUpdateAuthenticatedUserData() {
        Long institutionId = 1L;
        Long userId = 10L;
        Instant createdAt = Instant.parse("2026-07-02T15:30:00Z");

        User existing = new User();
        existing.setId(userId);
        existing.setName("Old Name");
        existing.setEmail("user@example.com");
        existing.setRole(UserRole.ADMIN);
        existing.setActive(true);
        existing.setCreateAt(createdAt);

        UpdateUserCommand command = new UpdateUserCommand("New Name", UserRole.INSTRUCTOR, false);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByIdAndInstitution_Id(userId, institutionId))
                    .thenReturn(Optional.of(existing));
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            UserReturn result = updateUserHandler.updateUser(userId, command);

            assertAll(
                    () -> assertEquals("New Name", existing.getName()),
                    () -> assertEquals(UserRole.INSTRUCTOR, existing.getRole()),
                    () -> assertFalse(existing.isActive()),
                    () -> assertEquals("New Name", result.name()),
                    () -> assertEquals("user@example.com", result.email()),
                    () -> assertEquals(UserRole.INSTRUCTOR, result.role()),
                    () -> assertFalse(result.active()),
                    () -> assertEquals(createdAt, result.createAt())
            );
            verify(tenantValidation).validateInstitution(institutionId);
            verify(userRepository).save(existing);
        }
    }

    @Test
    @DisplayName("UNI_02.02 - Should reject update of non-existent user")
    void shouldRejectUpdateOfNonExistentUser() {
        Long institutionId = 1L;
        Long userId = 99L;
        UpdateUserCommand command = new UpdateUserCommand("Name", UserRole.ADMIN, true);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByIdAndInstitution_Id(userId, institutionId))
                    .thenReturn(Optional.empty());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> updateUserHandler.updateUser(userId, command));

            assertEquals(UserMessage.USER_NOT_FOUND, ex.getMessage());
            verify(userRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_02.03 - Should validate tenant before updating user")
    void shouldValidateTenantBeforeUpdatingUser() {
        Long institutionId = 1L;
        Long userId = 10L;
        UpdateUserCommand command = new UpdateUserCommand("Name", UserRole.ADMIN, true);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByIdAndInstitution_Id(userId, institutionId))
                    .thenReturn(Optional.of(new User()));
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            updateUserHandler.updateUser(userId, command);

            verify(tenantValidation).validateInstitution(institutionId);
        }
    }

    @Test
    @DisplayName("UNI_02.04 - Should reject update of user from another institution")
    void shouldRejectUpdateOfUserFromAnotherInstitution() {
        Long currentInstitutionId = 1L;
        Long otherInstitutionId = 2L;
        Long userId = 10L;

        UpdateUserCommand command = new UpdateUserCommand("Name", UserRole.ADMIN, true);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(currentInstitutionId);

            // O repositório é consultado com o ID da instituição autenticada (1L),
            // portanto um usuário da instituição 2L não é encontrado.
            when(userRepository.findByIdAndInstitution_Id(userId, currentInstitutionId))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class,
                    () -> updateUserHandler.updateUser(userId, command));

            verify(userRepository, never()).save(any());
        }
    }
}