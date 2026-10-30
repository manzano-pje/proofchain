
/**
 * Testes unitários do caso de uso de criação de cursos.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a criação de curso com dados válidos, o uso do factory
 * method Course.create, a rejeição de duplicidade por nome dentro
 * da instituição e a validação do contexto de tenant.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.course.unitTest;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.application.command.CreateCourseCommand;
import com.proofchain.business.course.application.handler.CreateCourseHandler;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.CourseMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UNI_01_CreateCourseHandlerTest {

    @Mock private CourseRepository courseRepository;
    @Mock private InstitutionRepository institutionRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private CreateCourseHandler createCourseHandler;

    private CreateCourseCommand validCommand;
    private Institution mockInstitution;

    @BeforeEach
    void setUp() {
        validCommand = new CreateCourseCommand("Java Avançado", "Curso de Java", 40);
        mockInstitution = new Institution();
        mockInstitution.setId(1L);
        mockInstitution.setName("Instituição Test");
    }

    @Test
    @DisplayName("UNI_01.01 - Deve criar curso com dados válidos")
    void shouldCreateCourseWithValidData() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(courseRepository.existsByNameAndInstitutionId(anyString(), anyLong()))
                    .thenReturn(false);

            createCourseHandler.handle(validCommand);

            ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
            verify(courseRepository).save(captor.capture());
            Course saved = captor.getValue();
            assertAll(
                    () -> assertEquals("Java Avançado", saved.getName()),
                    () -> assertEquals("Curso de Java", saved.getDescription()),
                    () -> assertEquals(40, saved.getHours()),
                    () -> assertEquals(mockInstitution, saved.getInstitution())
            );
            verify(tenantValidation).validateInstitution(1L);
        }
    }

    @Test
    @DisplayName("UNI_01.02 - Deve rejeitar criação quando validação de tenant falhar")
    void shouldRejectCreationWhenTenantValidationFails() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            doThrow(new NotFoundException("Instituição não encontrada"))
                    .when(tenantValidation).validateInstitution(1L);

            assertThrows(NotFoundException.class, () -> createCourseHandler.handle(validCommand));

            verify(courseRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_01.03 - Deve rejeitar criação com nome duplicado na instituição")
    void shouldRejectCreationWithDuplicateNameInInstitution() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(courseRepository.existsByNameAndInstitutionId("Java Avançado", 1L))
                    .thenReturn(true);

            AlreadyExistsException ex = assertThrows(AlreadyExistsException.class,
                    () -> createCourseHandler.handle(validCommand));

            assertEquals(CourseMessages.COURSE_ALREADY_EXISTS, ex.getMessage());
            verify(courseRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_01.04 - Não deve persistir curso quando validação falhar")
    void shouldNotPersistCourseWhenValidationFails() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(courseRepository.existsByNameAndInstitutionId(anyString(), anyLong()))
                    .thenReturn(true);

            assertThrows(AlreadyExistsException.class,
                    () -> createCourseHandler.handle(validCommand));

            verify(courseRepository, never()).save(any());
        }
    }
}