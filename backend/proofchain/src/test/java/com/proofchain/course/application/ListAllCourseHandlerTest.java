package com.proofchain.course.application;

import com.proofchain.course.application.handler.ListAllCourseHandler;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.infrastructure.repository.CourseRepository;
import com.proofchain.shared.exception.ResourceNotFoundException;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListAllCourseHandlerTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private ListAllCourseHandler handler;

    private Institution institution;
    private Course course1;
    private Course course2;
    private Course course3;
    private List<Course> courses = new ArrayList<>();

    private Course createCorse(Long id, String name, String description, int hours){

        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setDescription(description);
        course.setHours(hours);
        return course;
    }

    @BeforeEach
    void setup(){
        course1 = createCorse(1l, "Java", "Curso Java", 30);
        course2 = createCorse(2l, "Lógica", "Curso Lógica", 40);
        course3 = createCorse(3l, "Docker", "Curso Docker", 10);
        courses = List.of(course1, course2, course3);

        institution = new Institution();
        institution.setId(1l);
    }

    @Test
    void ShouldListAllCoursesSucessfuly(){
        try(
            MockedStatic<SecurityUtils> security =
                    mockStatic(SecurityUtils.class)){

            security.when(SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(true);

            when(courseRepository.findAllByInstitutionId(1L))
                    .thenReturn(courses);

            handler.listAllCourses();

            verify(courseRepository, times(1))
                    .findAllByInstitutionId(1L);
        }
    }

    @Test
    void ShouldListAlCourseNotFoundInstitutioin() {

        try (MockedStatic<SecurityUtils> security =
                     mockStatic(SecurityUtils.class)) {

            // Identificação de instituição válida
            security.when(SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(false);

            assertThrows(InstitutionNotFoundException.class, ()->handler.listAllCourses());

        }
    }

    @Test
    void ShouldListAllCoursesNotFound(){
        try(
                MockedStatic<SecurityUtils> security =
                        mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getInstitutionId)
                    .thenReturn(1L);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(true);

            when(courseRepository.findAllByInstitutionId(1L))
                    .thenReturn(Collections.emptyList());

            assertThrows(ResourceNotFoundException.class, () -> handler.listAllCourses());
        }

    }

}
