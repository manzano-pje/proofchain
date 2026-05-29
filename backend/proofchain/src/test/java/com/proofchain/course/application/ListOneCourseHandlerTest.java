package com.proofchain.course.application;

import com.proofchain.course.application.handler.ListOneCourseHandler;
import com.proofchain.course.domain.exception.CourseNotFoundException;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.security.SecurityUtils;
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
public class ListOneCourseHandlerTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private ListOneCourseHandler handler;

    private Institution institution;
    private Course course;


    @BeforeEach
    void setup(){
        course = new Course();
        course.setId(1L);
        course.setName("Java");
        course.setDescription("Curso Java");
        course.setHours(30);

        institution = new Institution();
        institution.setId(1L);
    }

    @Test
    void ShouldListOneCourseSucessfully() {

        try (MockedStatic<SecurityUtils> security =
                     mockStatic(SecurityUtils.class)){

            // Identificação de instituição válida
            security.when(SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(true);

            when(courseRepository.findByIdAndInstitutionId(1L, 1L))
                    .thenReturn(Optional.of(course));

            handler.listOneCourse(1l);

            verify(courseRepository, times(1))
                    .findByIdAndInstitutionId(1l, 1l);

        }
    }

    @Test
    void ShouldListOneCourseNotFoundInstitutioin() {

        try (MockedStatic<SecurityUtils> security =
                     mockStatic(SecurityUtils.class)) {

            // Identificação de instituição válida
            security.when(SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(false);

            assertThrows(InstitutionNotFoundException.class, ()->handler.listOneCourse(1L));

        }
    }

    @Test
    void ShouldListOneCourseNotFound() {

        try (MockedStatic<SecurityUtils> security =
                     mockStatic(SecurityUtils.class)) {

            // Identificação de instituição válida
            security.when(SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(true);

            when(courseRepository.findByIdAndInstitutionId(1L, 1L))
                    .thenReturn(Optional.empty());

            assertThrows(CourseNotFoundException.class, () -> handler.listOneCourse(1L));
        }
    }
}
