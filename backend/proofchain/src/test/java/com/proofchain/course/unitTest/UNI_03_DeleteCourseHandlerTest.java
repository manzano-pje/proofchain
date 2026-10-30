
/**
 * Testes unitários do caso de uso de exclusão de cursos.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a exclusão lógica do curso, a rejeição de curso
 * inexistente, o isolamento entre instituições e a idempotência
 * da operação.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.course.unitTest;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.application.handler.DeleteCourseHandler;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.CourseMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UNI_03_DeleteCourseHandlerTest {

    @Mock private InstitutionRepository institutionRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private DeleteCourseHandler deleteCourseHandler;

    @Test
    @DisplayName("UNI_03.01 - Deve executar exclusão lógica do curso")
    void shouldPerformLogicalDeletionOfCourse() {
        Long institutionId = 1L;
        Long courseId = 10L;
        Course existing = new Course();
        existing.setId(courseId);
        existing.setDeletedAt(null);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(courseRepository.findByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(Optional.of(existing));

            deleteCourseHandler.deleteCourse(courseId);

            assertNotNull(existing.getDeletedAt());
            verify(tenantValidation).validateInstitution(institutionId);
            verify(courseRepository).save(existing);
            verify(courseRepository, never()).delete(any());
        }
    }

    @Test
    @DisplayName("UNI_03.02 - Deve rejeitar exclusão de curso inexistente")
    void shouldRejectDeletionOfNonExistentCourse() {
        Long institutionId = 1L;
        Long courseId = 99L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(courseRepository.findByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(Optional.empty());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> deleteCourseHandler.deleteCourse(courseId));

            assertEquals(CourseMessages.COURSE_NOT_FOUND, ex.getMessage());
            verify(courseRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_03.03 - Deve rejeitar exclusão de curso de outra instituição")
    void shouldRejectDeletionOfCourseFromAnotherInstitution() {
        Long currentInstitutionId = 1L;
        Long courseId = 10L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(currentInstitutionId);
            when(courseRepository.findByIdAndInstitutionId(courseId, currentInstitutionId))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class,
                    () -> deleteCourseHandler.deleteCourse(courseId));
            verify(courseRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_03.04 - Deve rejeitar exclusão quando validação de tenant falhar")
    void shouldRejectDeletionWhenTenantValidationFails() {
        Long institutionId = 1L;
        Long courseId = 10L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            doThrow(new NotFoundException("Instituição não encontrada"))
                    .when(tenantValidation).validateInstitution(institutionId);

            assertThrows(NotFoundException.class,
                    () -> deleteCourseHandler.deleteCourse(courseId));
            verify(courseRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_03.05 - Deve ser idempotente ao repetir exclusão de curso")
    void shouldBeIdempotentWhenRepeatingCourseDeletion() {
        Long institutionId = 1L;
        Long courseId = 10L;
        Course existing = new Course();
        existing.setId(courseId);
        existing.setDeletedAt(Instant.now()); // já excluído logicamente

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            // A busca padrão não filtra deletedAt, então o curso ainda é encontrado
            when(courseRepository.findByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(Optional.of(existing));

            // Não lança exceção: apenas atualiza o timestamp
            deleteCourseHandler.deleteCourse(courseId);

            assertNotNull(existing.getDeletedAt());
            verify(courseRepository).save(existing);
        }
    }
}
