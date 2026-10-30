
/**
 * Testes unitários do caso de uso de listagem de cursos.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a listagem de cursos da instituição autenticada,
 * a rejeição de lista vazia, o isolamento entre instituições
 * e a conversão para o DTO FullCourseResponse.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.course.unitTest;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.application.handler.ListAllCourseHandler;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.business.course.interfaces.dto.response.FullCourseResponse;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UNI_04_ListAllCourseHandlerTest {

    @Mock private CourseRepository courseRepository;
    @Mock private InstitutionRepository institutionRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private ListAllCourseHandler listAllCourseHandler;

    @Test
    @DisplayName("UNI_04.01 - Deve listar cursos da instituição autenticada")
    void shouldListCoursesFromAuthenticatedInstitution() {
        Long institutionId = 1L;
        Course course1 = new Course();
        course1.setId(1L);
        course1.setName("Curso A");
        course1.setDescription("Descrição A");
        course1.setHours(10);
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Curso B");
        course2.setDescription("Descrição B");
        course2.setHours(20);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(courseRepository.findAllByInstitutionIdAndDeletedAtIsNull(institutionId))
                    .thenReturn(List.of(course1, course2));

            List<FullCourseResponse> result = listAllCourseHandler.listAllCourses();

            assertEquals(2, result.size());
            assertEquals("Curso A", result.get(0).name());
            assertEquals("Curso B", result.get(1).name());
            verify(tenantValidation).validateInstitution(institutionId);
        }
    }

    @Test
    @DisplayName("UNI_04.02 - Deve lançar NotFoundException quando não houver cursos")
    void shouldThrowNotFoundExceptionWhenNoCoursesExist() {
        Long institutionId = 1L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(courseRepository.findAllByInstitutionIdAndDeletedAtIsNull(institutionId))
                    .thenReturn(List.of());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> listAllCourseHandler.listAllCourses());

            assertEquals(CourseMessages.COURSE_NOT_FOUND, ex.getMessage());
        }
    }

    @Test
    @DisplayName("UNI_04.03 - Não deve listar cursos de outra instituição")
    void shouldNotListCoursesFromAnotherInstitution() {
        Long currentInstitutionId = 1L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(currentInstitutionId);
            when(courseRepository.findAllByInstitutionIdAndDeletedAtIsNull(currentInstitutionId))
                    .thenReturn(List.of());

            assertThrows(NotFoundException.class,
                    () -> listAllCourseHandler.listAllCourses());
        }
    }

    @Test
    @DisplayName("UNI_04.04 - Deve rejeitar listagem quando validação de tenant falhar")
    void shouldRejectListingWhenTenantValidationFails() {
        Long institutionId = 1L;

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            doThrow(new NotFoundException("Instituição não encontrada"))
                    .when(tenantValidation).validateInstitution(institutionId);

            assertThrows(NotFoundException.class,
                    () -> listAllCourseHandler.listAllCourses());
        }
    }
}
