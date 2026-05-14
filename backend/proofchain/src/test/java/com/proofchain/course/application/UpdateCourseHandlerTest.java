package com.proofchain.course.application;

import com.proofchain.course.application.command.UpdateCourseCommand;
import com.proofchain.course.application.handler.UpdateCourseHandler;
import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCourseHandlerTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private UpdateCourseHandler  handler;

    private UpdateCourseCommand command;
    private Institution institution;
    private Course course;

    @BeforeEach
    void setup(){
        course = new Course();
        course.setId(1L);
        course.setName("Java");
        course.setDescription("Curso Java");
        course.setHours(30);

        command = new UpdateCourseCommand(
                1L,
                "Lógica",
                "Curso de Lògica",
                20
        );
        institution = new Institution();
        institution.setId(1L);
    }

    @Test
    void shouldUpdateCourseSuccessfully(){

        //assert
        // MockStatic controla os métodos estáticos da classe SecurityUtils
        try (MockedStatic<SecurityUtils> security =
                     mockStatic(com.proofchain.security.SecurityUtils.class)) {

            // O que fazer quando chamar getInstitutionId()
            // no caso retorna 1L
            security.when(com.proofchain.security.SecurityUtils::getInstitutionId
            ).thenReturn(1L);

            // Quando buscar instituição 1, retornar esta instituição fake
            when(institutionRepository.findById(1L))
                    .thenReturn(Optional.of(institution));

            // Quando buscar curso com cursoID e institutionId retornar curse fake
            when(courseRepository.findByIdAndInstitutionId(command.getId(), institution.getId()))
                    .thenReturn(Optional.of(course));

            // Action
            handler.updateCourse(command.getId(), command);

            //Assert
            verify(courseRepository,times(1))
                    .save(any(Course.class));
        }
    }

    @Test
    void shouldThrowExceptionWhenInstitutionNotFound(){

        //assert
        // MockStatic controla os métodos estáticos da classe SecurityUtils
        try(
            MockedStatic<SecurityUtils> security =
                    mockStatic(com.proofchain.security.SecurityUtils.class)) {

            security.when(com.proofchain.security.SecurityUtils::getInstitutionId
            ).thenReturn(1L);

            when(institutionRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    InstitutionNotFoundException.class,
                    () -> handler.updateCourse(command.getId(), command)
            );
        }
    }

    @Test
    void ShouldThrouExceptionWhenCourseNotFound(){

        try(
                MockedStatic<SecurityUtils> security =
                        mockStatic(com.proofchain.security.SecurityUtils.class)) {

            security.when(com.proofchain.security.SecurityUtils::getInstitutionId
            ).thenReturn(1L);

            when(institutionRepository.findById(1L))
                    .thenReturn(Optional.of(institution));

            when(courseRepository.findByIdAndInstitutionId(command.getId(), institution.getId()))
                    .thenReturn(Optional.empty());

            assertThrows(
                    CourseNotFoundException.class,
                    () -> handler.updateCourse(command.getId(),command)
            );
        }
    }
}