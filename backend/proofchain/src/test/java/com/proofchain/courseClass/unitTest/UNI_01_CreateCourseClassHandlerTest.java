
/**
 * Testes unitários do caso de uso de criação de turmas.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a criação de turma com dados válidos, a rejeição de
 * instituição ou curso inexistente, o impedimento de associação a
 * curso de outra instituição e a validação do contexto de tenant.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.courseClass.unitTest;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.business.courseClass.application.command.RequestCourseClassCommand;
import com.proofchain.business.courseClass.application.handler.CreateCourseClassHandler;
import com.proofchain.business.courseClass.domain.model.CourseClass;
import com.proofchain.business.courseClass.infrastructure.repository.CourseClassRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.CourseMessages;
import com.proofchain.shared.exception.messages.InstructorMessages;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.domain.model.User;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UNI_01_CreateCourseClassHandlerTest {

    @Mock private CourseClassRepository courseClassRepository;
    @Mock private InstitutionRepository institutionRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private UserRepository userRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private CreateCourseClassHandler createCourseClassHandler;

    private RequestCourseClassCommand validCommand;
    private Institution mockInstitution;
    private Course mockCourse;
    private User mockUser;

    @BeforeEach
    void setUp() {
        validCommand = new RequestCourseClassCommand(5L, 10L);

        mockInstitution = new Institution();
        mockInstitution.setId(1L);
        mockInstitution.setName("Instituição Test");
        mockInstitution.setDeletedAt(null);

        mockCourse = new Course();
        mockCourse.setId(10L);
        mockCourse.setName("Curso Teste");

        mockUser = new User();
        mockUser.setId(5L);
        mockUser.setName("Instrutor Teste");
    }

    @Test
    @DisplayName("UNI_01.01 - Deve criar turma com dados válidos")
    void shouldCreateCourseClassWithValidData() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(courseClassRepository
                    .existsByIdAndCourse_IdAndInstitution_IdAndInstitution_DeletedAtIsNull(5L, 10L, 1L))
                    .thenReturn(false);
            when(courseRepository.findByIdAndInstitutionId(10L, 1L))
                    .thenReturn(Optional.of(mockCourse));
            when(userRepository.findByIdAndInstitution_Id(5L, 1L))
                    .thenReturn(Optional.of(mockUser));

            createCourseClassHandler.create(validCommand);

            ArgumentCaptor<CourseClass> captor = ArgumentCaptor.forClass(CourseClass.class);
            verify(courseClassRepository).save(captor.capture());
            CourseClass saved = captor.getValue();

            assertAll(
                    () -> assertEquals(mockUser, saved.getUser()),
                    () -> assertEquals(mockCourse, saved.getCourse()),
                    () -> assertEquals(mockInstitution, saved.getInstitution()),
                    () -> assertTrue(saved.isActive()),
                    () -> assertNull(saved.getUpdateAt()),
                    () -> assertNotNull(saved.getCreateAt())
            );
            verify(tenantValidation).validateInstitution(1L);
            verify(courseClassRepository).save(any(CourseClass.class));
        }
    }

    @Test
    @DisplayName("UNI_01.02 - Deve rejeitar criação quando instrutor já vinculado ao curso")
    void shouldRejectCreationWhenInstructorAlreadyLinkedToCourse() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(courseClassRepository
                    .existsByIdAndCourse_IdAndInstitution_IdAndInstitution_DeletedAtIsNull(5L, 10L, 1L))
                    .thenReturn(true);

            AlreadyExistsException ex = assertThrows(AlreadyExistsException.class,
                    () -> createCourseClassHandler.create(validCommand));

            assertEquals(InstructorMessages.INSTRUCTOR_ALREAY_EXISTS, ex.getMessage());
            verify(courseClassRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_01.03 - Deve rejeitar criação quando curso não existir")
    void shouldRejectCreationWhenCourseDoesNotExist() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(courseClassRepository
                    .existsByIdAndCourse_IdAndInstitution_IdAndInstitution_DeletedAtIsNull(5L, 10L, 1L))
                    .thenReturn(false);
            when(courseRepository.findByIdAndInstitutionId(10L, 1L))
                    .thenReturn(Optional.empty());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> createCourseClassHandler.create(validCommand));

            assertEquals(CourseMessages.COURSE_NOT_FOUND, ex.getMessage());
            verify(courseClassRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_01.04 - Deve rejeitar criação quando usuário não existir")
    void shouldRejectCreationWhenUserDoesNotExist() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(courseClassRepository
                    .existsByIdAndCourse_IdAndInstitution_IdAndInstitution_DeletedAtIsNull(5L, 10L, 1L))
                    .thenReturn(false);
            when(courseRepository.findByIdAndInstitutionId(10L, 1L))
                    .thenReturn(Optional.of(mockCourse));
            when(userRepository.findByIdAndInstitution_Id(5L, 1L))
                    .thenReturn(Optional.empty());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> createCourseClassHandler.create(validCommand));

            assertEquals(UserMessage.USER_NOT_FOUND, ex.getMessage());
            verify(courseClassRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_01.05 - Deve rejeitar criação quando validação de tenant falhar")
    void shouldRejectCreationWhenTenantValidationFails() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            doThrow(new NotFoundException("Instituição não encontrada"))
                    .when(tenantValidation).validateInstitution(1L);

            assertThrows(NotFoundException.class,
                    () -> createCourseClassHandler.create(validCommand));

            verify(courseClassRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_01.06 - Não deve persistir turma quando validação falhar")
    void shouldNotPersistCourseClassWhenValidationFails() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(1L);
            when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                    .thenReturn(Optional.of(mockInstitution));
            when(courseClassRepository
                    .existsByIdAndCourse_IdAndInstitution_IdAndInstitution_DeletedAtIsNull(5L, 10L, 1L))
                    .thenReturn(true);

            assertThrows(AlreadyExistsException.class,
                    () -> createCourseClassHandler.create(validCommand));

            verify(courseClassRepository, never()).save(any());
        }
    }
}