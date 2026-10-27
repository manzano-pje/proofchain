/**
 * Testes unitários do caso de uso de listagem de usuários.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a listagem de usuários da instituição autenticada,
 * o retorno de lista vazia e o isolamento entre tenants.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.user.unitTest;

import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.UnauthorizedException;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.applications.handler.ListAllUserHandler;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UNI_04_ListAllUserHandlerTest {

    @Mock private UserRepository userRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private ListAllUserHandler listAllUserHandler;

    @Test
    @DisplayName("UNI_04.01 - Should list users from authenticated institution")
    void shouldListUsersFromAuthenticatedInstitution() {
        Long institutionId = 1L;
        User user1 = new User();
        user1.setName("A");
        user1.setEmail("a@example.com");
        user1.setRole(UserRole.ADMIN);
        user1.setActive(true);
        User user2 = new User();
        user2.setName("B");
        user2.setEmail("b@example.com");
        user2.setRole(UserRole.INSTRUCTOR);
        user2.setActive(true);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findAllByInstitution_IdAndInstitution_DeletedAtIsNull(institutionId))
                    .thenReturn(List.of(user1, user2));

            List<UserReturn> result = listAllUserHandler.listAllUser();

            assertAll(
                    () -> assertEquals(2, result.size()),
                    () -> assertEquals("A", result.get(0).name()),
                    () -> assertEquals("a@example.com", result.get(0).email()),
                    () -> assertEquals(UserRole.ADMIN, result.get(0).role()),
                    () -> assertEquals("B", result.get(1).name()),
                    () -> assertEquals("b@example.com", result.get(1).email()),
                    () -> assertEquals(UserRole.INSTRUCTOR, result.get(1).role())
            );
            verify(tenantValidation).validateInstitution(institutionId);
        }
    }

    @Test
    @DisplayName("UNI_04.02 - Should throw NotFoundException when no users exist")
    void shouldThrowNotFoundExceptionWhenNoUsersExist() {
        Long institutionId = 1L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findAllByInstitution_IdAndInstitution_DeletedAtIsNull(institutionId))
                    .thenReturn(List.of());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> listAllUserHandler.listAllUser());

            assertEquals(UserMessage.USER_NOT_FOUND, ex.getMessage());
        }
    }

    @Test
    @DisplayName("UNI_04.03 - Should validate tenant before listing users")
    void shouldValidateTenantBeforeListingUsers() {
        Long institutionId = 1L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findAllByInstitution_IdAndInstitution_DeletedAtIsNull(institutionId))
                    .thenReturn(List.of(new User()));

            listAllUserHandler.listAllUser();

            verify(tenantValidation).validateInstitution(institutionId);
        }
    }

    @Test
    @DisplayName("UNI_04.04 - Should reject listing when tenant validation fails")
    void shouldRejectListingWhenTenantValidationFails() {
        Long institutionId = 1L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);

            doThrow(new UnauthorizedException("invalid tenant"))
                    .when(tenantValidation).validateInstitution(institutionId);

            assertThrows(UnauthorizedException.class,
                    () -> listAllUserHandler.listAllUser());

            verify(userRepository, never())
                    .findAllByInstitution_IdAndInstitution_DeletedAtIsNull(any());
        }
    }
}