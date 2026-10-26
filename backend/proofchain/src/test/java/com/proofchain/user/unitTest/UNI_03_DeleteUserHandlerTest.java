/**
 * Testes unitários do caso de uso de exclusão de usuários.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a desativação lógica do usuário, o isolamento
 * entre instituições e o tratamento de usuário inexistente.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.user.unitTest;

import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.applications.handler.DeleteUserHandler;
import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserHandlerTest {

    @Mock private UserRepository userRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private DeleteUserHandler deleteUserHandler;

    @Test
    @DisplayName("UNI_03.01 - Should soft delete authenticated user")
    void shouldSoftDeleteAuthenticatedUser() {
        Long institutionId = 1L;
        Long userId = 10L;
        User existing = new User();
        existing.setId(userId);
        existing.setActive(true);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByIdAndInstitution_Id(userId, institutionId))
                    .thenReturn(Optional.of(existing));

            deleteUserHandler.deleteUser(userId);

            assertFalse(existing.isActive());
            verify(tenantValidation).validateInstitution(institutionId);
            verify(userRepository).save(existing);
            verify(userRepository, never()).delete(any());
        }
    }

    @Test
    @DisplayName("UNI_03.02 - Should reject deletion of non-existent user")
    void shouldRejectDeletionOfNonExistentUser() {
        Long institutionId = 1L;
        Long userId = 99L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByIdAndInstitution_Id(userId, institutionId))
                    .thenReturn(Optional.empty());

            UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                    () -> deleteUserHandler.deleteUser(userId));

            assertEquals(UserMessage.USER_NOT_FOUND, ex.getMessage());
            verify(userRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_03.03 - Should validate tenant before deleting user")
    void shouldValidateTenantBeforeDeletingUser() {
        Long institutionId = 1L;
        Long userId = 10L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByIdAndInstitution_Id(userId, institutionId))
                    .thenReturn(Optional.of(new User()));

            deleteUserHandler.deleteUser(userId);

            verify(tenantValidation).validateInstitution(institutionId);
        }
    }
}
