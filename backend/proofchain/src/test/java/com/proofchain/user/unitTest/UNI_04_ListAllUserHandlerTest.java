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
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.applications.handler.ListAllUserHandler;
import com.proofchain.user.domain.model.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        User user2 = new User();
        user2.setName("B");

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(userRepository.findAllByInstitution_IdAndInstitution_DeletedAtIsNull(institutionId))
                    .thenReturn(List.of(user1, user2));

            List<UserReturn> result = listAllUserHandler.listAllUser();

            assertEquals(2, result.size());
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
}