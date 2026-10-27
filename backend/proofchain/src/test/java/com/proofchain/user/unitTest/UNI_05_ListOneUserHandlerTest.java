
/**
 * Testes unitários do caso de uso de consulta individual de usuários.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a consulta por e-mail dentro do tenant autenticado,
 * o mapeamento entidade → UserReturn (record) e o tratamento de
 * usuário inexistente.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.user.unitTest;

import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.applications.handler.ListOneUserHandler;
import com.proofchain.user.domain.exception.UserNotFoundException;
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
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UNI_05_ListOneUserHandlerTest {

    @Mock private UserRepository userRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private ListOneUserHandler listOneUserHandler;

    @Test
    @DisplayName("UNI_05.01 - Should return user by e-mail within authenticated institution")
    void shouldReturnUserByEmailWithinAuthenticatedInstitution() {
        Long institutionId = 1L;
        String email = "user@example.com";
        Instant createdAt = Instant.parse("2026-07-02T15:30:00Z");

        User user = new User();
        user.setName("User Name");
        user.setEmail(email);
        user.setRole(UserRole.ADMIN);
        user.setActive(true);
        user.setCreateAt(createdAt);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByEmailAndInstitution_Id(email, institutionId))
                    .thenReturn(Optional.of(user));

            UserReturn result = listOneUserHandler.listOneUser(email);

            assertAll(
                    () -> assertEquals("User Name", result.name()),
                    () -> assertEquals(email, result.email()),
                    () -> assertEquals(UserRole.ADMIN, result.role()),
                    () -> assertEquals(true, result.active()),
                    () -> assertEquals(createdAt, result.createAt())
            );
            verify(tenantValidation).validateInstitution(institutionId);
        }
    }

    @Test
    @DisplayName("UNI_05.02 - Should throw NotFoundException when user does not exist")
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        Long institutionId = 1L;
        String email = "missing@example.com";

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByEmailAndInstitution_Id(email, institutionId))
                    .thenReturn(Optional.empty());

            UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                    () -> listOneUserHandler.listOneUser(email));

            assertEquals(UserMessage.USER_NOT_FOUND, ex.getMessage());

        }
    }

    @Test
    @DisplayName("UNI_05.03 - Should validate tenant before querying user")
    void shouldValidateTenantBeforeQueryingUser() {
        Long institutionId = 1L;
        String email = "user@example.com";

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByEmailAndInstitution_Id(email, institutionId))
                    .thenReturn(Optional.of(new User()));

            listOneUserHandler.listOneUser(email);

            verify(tenantValidation).validateInstitution(institutionId);
        }
    }

    @Test
    @DisplayName("UNI_05.04 - Should reject query of user from another institution")
    void shouldRejectQueryOfUserFromAnotherInstitution() {
        Long currentInstitutionId = 1L;
        String email = "user@example.com";

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(currentInstitutionId);
            when(userRepository.findByEmailAndInstitution_Id(email, currentInstitutionId))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> listOneUserHandler.listOneUser(email));
        }
    }

    @Test
    @DisplayName("UNI_05.05 - Should return inactive user when queried directly")
    void shouldReturnInactiveUserWhenQueriedDirectly() {
        Long institutionId = 1L;
        String email = "inactive@example.com";
        User inactive = new User();
        inactive.setName("Inactive User");
        inactive.setEmail(email);
        inactive.setRole(UserRole.ADMIN);
        inactive.setActive(false);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findByEmailAndInstitution_Id(email, institutionId))
                    .thenReturn(Optional.of(inactive));

            UserReturn result = listOneUserHandler.listOneUser(email);

            assertEquals(email, result.email());
            assertFalse(result.active());
        }
    }

}
