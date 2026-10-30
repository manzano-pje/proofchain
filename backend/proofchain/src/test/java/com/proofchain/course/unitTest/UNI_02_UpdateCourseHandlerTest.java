
/**
 * Testes unitários do caso de uso de atualização de cursos.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à confiabilidade previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar a atualização de curso existente, a rejeição de curso
 * inexistente, o isolamento entre instituições e o comportamento
 * atual da validação de duplicidade por nome.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.course.unitTest;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.course.application.command.UpdateCourseCommand;
import com.proofchain.business.course.application.handler.UpdateCourseHandler;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.infrastructure.repository.CourseRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UNI_02_UpdateCourseHandlerTest {

    @Mock private InstitutionRepository institutionRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private TenantValidation tenantValidation;
    @InjectMocks private UpdateCourseHandler updateCourseHandler;

    @Test
    @DisplayName("UNI_02.01 - Deve atualizar curso existente com dados válidos")
    void shouldUpdateExistingCourseWithValidData() {
        Long institutionId = 1L;
        Long courseId = 10L;
        Course existing = new Course();
        existing.setId(courseId);
        existing.setName("Nome Antigo");
        existing.setDescription("Descrição antiga");
        existing.setHours(20);

        UpdateCourseCommand command = new UpdateCourseCommand(courseId, "Nome Novo", "Descrição nova", 40);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(courseRepository.findByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(Optional.of(existing));
            when(courseRepository.existsByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(true);
            when(courseRepository.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

            Course result = updateCourseHandler.updateCourse(courseId, command);

            assertEquals("Nome Novo", result.getName());
            assertEquals("Descrição Nova", result.getDescription());
            assertEquals(40, result.getHours());
            verify(tenantValidation).validateInstitution(institutionId);
            verify(courseRepository).save(existing);
        }
    }

    @Test
    @DisplayName("UNI_02.02 - Deve rejeitar atualização de curso inexistente")
    void shouldRejectUpdateOfNonExistentCourse() {
        Long institutionId = 1L;
        Long courseId = 99L;
        UpdateCourseCommand command = new UpdateCourseCommand(courseId, "Nome", "Descrição", 10);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(courseRepository.findByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(Optional.empty());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> updateCourseHandler.updateCourse(courseId, command));

            assertEquals(CourseMessages.COURSE_NOT_FOUND, ex.getMessage());
            verify(courseRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_02.03 - Deve rejeitar atualização de curso de outra instituição")
    void shouldRejectUpdateOfCourseFromAnotherInstitution() {
        Long currentInstitutionId = 1L;
        Long otherInstitutionId = 2L;
        Long courseId = 10L;
        UpdateCourseCommand command = new UpdateCourseCommand(courseId, "Nome", "Descrição", 10);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(currentInstitutionId);
            // A consulta é feita com o institutionId autenticado (1L),
            // portanto um curso da instituição 2L não é encontrado.
            when(courseRepository.findByIdAndInstitutionId(courseId, currentInstitutionId))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class,
                    () -> updateCourseHandler.updateCourse(courseId, command));
            verify(courseRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_02.04 - Deve rejeitar atualização quando validação de tenant falhar")
    void shouldRejectUpdateWhenTenantValidationFails() {
        Long institutionId = 1L;
        Long courseId = 10L;
        UpdateCourseCommand command = new UpdateCourseCommand(courseId, "Nome", "Descrição", 10);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            doThrow(new NotFoundException("Instituição não encontrada"))
                    .when(tenantValidation).validateInstitution(institutionId);

            assertThrows(NotFoundException.class,
                    () -> updateCourseHandler.updateCourse(courseId, command));
            verify(courseRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("UNI_02.05 - Deve rejeitar atualização quando o nome for mantido (comportamento atual)")
    void shouldRejectUpdateWhenNameIsUnchanged() {
        Long institutionId = 1L;
        Long courseId = 10L;
        Course existing = new Course();
        existing.setId(courseId);
        existing.setName("Mesmo Nome");
        existing.setDescription("Descrição antiga");
        existing.setHours(20);

        // O comando mantém o mesmo nome
        UpdateCourseCommand command = new UpdateCourseCommand(courseId, "Mesmo Nome", "Nova descrição", 30);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(courseRepository.findByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(Optional.of(existing));
            // A validação de duplicidade é acionada porque o nome é igual
            when(courseRepository.existsByIdAndInstitutionId(courseId, institutionId))
                    .thenReturn(true);

            // Documenta o comportamento atual: atualizações que mantêm o nome
            // são bloqueadas indevidamente.
            assertThrows(AlreadyExistsException.class,
                    () -> updateCourseHandler.updateCourse(courseId, command));
            verify(courseRepository, never()).save(any());
        }
    }
}


