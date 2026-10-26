package com.proofchain.user.unitTest;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.UnauthorizedException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.user.applications.command.CreateUserCommand;
import com.proofchain.user.applications.handler.CreateUserHandler;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.domain.model.UserRole;
import com.proofchain.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do caso de uso de criação de usuários.
 * <p>
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 * <p>
 * Escopo:
 * Verificar a criação de usuário com senha codificada, a associação
 * à instituição autenticada e o tratamento de instituição inexistente,
 * não autorizada e e-mail duplicado.
 * <p>
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

@ExtendWith(MockitoExtension.class)
class UNI_01_CreateUserHandlerTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private InstitutionRepository institutionRepository;
    @InjectMocks private CreateUserHandler createUserHandler;

    private CreateUserCommand validCommand;
    private Institution mockInstitution;

    @BeforeEach
    void setUp() {
        validCommand = new CreateUserCommand(
                "João Silva", "joao@example.com", "senha123", UserRole.ADMIN);
        mockInstitution = new Institution();
        mockInstitution.setId(1L);
        mockInstitution.setName("Instituição Test");
        mockInstitution.setCnpj("12345678000195");
        mockInstitution.setDeletedAt(null);
    }

    @Test
    @DisplayName("UNI-01.01 - Should create user with encoded password")
    void shouldCreateUserWithEncodedPassword() {
        String encodedPassword = "encrypted_password_hash";
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(userRepository.existsByEmailAndInstitutionId(validCommand.getEmail(), 1L))
                    .thenReturn(false);
            when(passwordEncoder.encode(validCommand.getPassword()))
                    .thenReturn(encodedPassword);

            createUserHandler.createUser(validCommand);

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(captor.capture());
            User saved = captor.getValue();
            assertAll(
                    () -> assertEquals(validCommand.getName(), saved.getName()),
                    () -> assertEquals(validCommand.getEmail(), saved.getEmail()),
                    () -> assertEquals(encodedPassword, saved.getPassword()),
                    () -> assertEquals(validCommand.getRole(), saved.getRole()),
                    () -> assertEquals(mockInstitution, saved.getInstitution())
            );
        }
    }

    @Test
    @DisplayName("UNI-01.02 - Should reject creation without authenticated institution")
    void shouldRejectCreationWithoutAuthenticatedInstitution() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(null);

            UnauthorizedException ex = assertThrows(
                    UnauthorizedException.class,
                    () -> createUserHandler.createUser(validCommand));

            assertEquals(InstitutionMessages.INSTITUTION_NOT_AUTORIZED, ex.getMessage());
            verify(userRepository, never()).save(any());
            verify(passwordEncoder, never()).encode(any());
        }
    }

    @Test
    @DisplayName("UNI-01.03 - Should reject non-existent institution")
    void shouldRejectNonExistentInstitution() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class,
                    () -> createUserHandler.createUser(validCommand));

            verify(userRepository, never()).save(any());
            verify(passwordEncoder, never()).encode(any());
        }
    }

    @Test
    @DisplayName("UNI-01.04 - Should reject creation with duplicate e-mail")
    void shouldRejectCreationWithDuplicateEmail() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(userRepository.existsByEmailAndInstitutionId(validCommand.getEmail(), 1L))
                    .thenReturn(true);

            AlreadyExistsException ex = assertThrows(
                    AlreadyExistsException.class,
                    () -> createUserHandler.createUser(validCommand));

            assertEquals(UserMessage.USER_ALREADY_EXISTS, ex.getMessage());
            verify(userRepository, never()).save(any());
            verify(passwordEncoder, never()).encode(any());
        }
    }
}