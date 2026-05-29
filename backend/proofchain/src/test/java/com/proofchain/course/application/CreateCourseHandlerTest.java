package com.proofchain.course.application;

import com.proofchain.course.application.command.CreateCourseCommand;
import com.proofchain.course.application.handler.CreateCourseHandler;
import com.proofchain.course.domain.exception.CourseIsRegisteredException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCourseHandlerTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private CreateCourseHandler handler;

    private CreateCourseCommand command;
    private Institution institution;

    @BeforeEach
    void setup() {

        command = new CreateCourseCommand(
                1L,
                "Java",
                "Curso Java",
                40
        );

        institution = new Institution();
        institution.setId(1L);
    }

    @Nested
    class CreateCourse {

        @Test
        void shouldCreateCourseSuccessfully() {

            // Arrange
            try (MockedStatic<SecurityUtils> security =
                         mockStatic(SecurityUtils.class)) {

                security.when(
                        SecurityUtils::getInstitutionId
                ).thenReturn(1L);

                when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                        .thenReturn(Optional.of(institution));

                when(courseRepository.existsByIdAndInstitutionId(
                        command.getId(),
                        institution.getId()
                )).thenReturn(false);

                // Act
                handler.handle(command);

                // Assert
                verify(courseRepository, times(1))
                        .save(any(Course.class));
            }
        }

        @Test
        void shouldThrowExceptionWhenInstitutionNotFound() {

            // Arrange
            try (MockedStatic<SecurityUtils> security =
                         mockStatic(SecurityUtils.class)) {

                security.when(
                        SecurityUtils::getInstitutionId
                ).thenReturn(1L);

                when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        InstitutionNotFoundException.class,
                        () -> handler.handle(command)
                );
            }
        }

        @Test
        void shouldThrowExceptionWhenCourseAlreadyExists() {

            // Arrange
            try (MockedStatic<SecurityUtils> security =
                         mockStatic(SecurityUtils.class)) {

                security.when(
                        SecurityUtils::getInstitutionId
                ).thenReturn(1L);

                when(institutionRepository.findByIdAndDeletedAtIsNull(1L))
                        .thenReturn(Optional.of(institution));

                when(courseRepository.existsByIdAndInstitutionId(
                        command.getId(),
                        institution.getId()
                )).thenReturn(true);

                // Act + Assert
                assertThrows(
                        CourseIsRegisteredException.class,
                        () -> handler.handle(command)
                );
            }
        }
    }
}