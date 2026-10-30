
/**
 * Testes unitários do caso de uso de consulta individual de cursos.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a consulta de curso por identificador dentro do tenant
 * autenticado, a rejeição de curso inexistente, o isolamento entre
 * instituições e a conversão para o DTO CourseResponse.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.course.unitTest;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.application.handler.ListOneCourseHandler;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.business.course.interfaces.dto.response.CourseResponse;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UNI_05_ListOneCourseHandlerTest {

    @Mock private InstitutionRepository institutionRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private ListOneCourseHandler listOneCourseHandler;

    @Test
    @DisplayName("UNI_05.01 - Deve retornar curso por identificador no tenant atual")
    void shouldReturnCourseByIdInCurrentTenant() {
        Long institutionId = 1L;
        Long courseId = 10L;
        Course course = new Course();
        course.setId(courseId);
        course.setName("Curso Teste");
        course.setDescription("Descrição Teste");
        course.setHours(30);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(courseRepository.findByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(Optional.of(course));

            CourseResponse result = listOneCourseHandler.listOneCourse(courseId);

            assertEquals(courseId, result.id());
            assertEquals("Curso Teste", result.name());
            assertEquals("Descrição Teste", result.description());
            assertEquals(30, result.hours());
            verify(tenantValidation).validateInstitution(institutionId);
        }
    }

    @Test
    @DisplayName("UNI_05.02 - Deve rejeitar consulta de curso inexistente")
    void shouldRejectQueryOfNonExistentCourse() {
        Long institutionId = 1L;
        Long courseId = 99L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(courseRepository.findByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(Optional.empty());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> listOneCourseHandler.listOneCourse(courseId));

            assertEquals(CourseMessages.COURSE_NOT_FOUND, ex.getMessage());
        }
    }

    @Test
    @DisplayName("UNI_05.03 - Deve rejeitar consulta de curso de outra instituição")
    void shouldRejectQueryOfCourseFromAnotherInstitution() {
        Long currentInstitutionId = 1L;
        Long courseId = 10L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(currentInstitutionId);
            when(courseRepository.findByIdAndInstitutionId(courseId, currentInstitutionId))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class,
                    () -> listOneCourseHandler.listOneCourse(courseId));
        }
    }

    @Test
    @DisplayName("UNI_05.04 - Deve rejeitar consulta quando validação de tenant falhar")
    void shouldRejectQueryWhenTenantValidationFails() {
        Long institutionId = 1L;
        Long courseId = 10L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            doThrow(new NotFoundException("Instituição não encontrada"))
                    .when(tenantValidation).validateInstitution(institutionId);

            assertThrows(NotFoundException.class,
                    () -> listOneCourseHandler.listOneCourse(courseId));
        }
    }
}