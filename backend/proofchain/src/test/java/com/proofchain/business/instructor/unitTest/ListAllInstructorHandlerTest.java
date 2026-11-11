package com.proofchain.business.instructor.unitTest;

import com.proofchain.business.instructor.application.query.ListAllInstructorHandler;
import com.proofchain.business.instructor.domain.model.Instructor;
import com.proofchain.business.instructor.insfrastructure.repository.InstructorRepository;
import com.proofchain.business.instructor.interfaces.dto.response.InstructorsSumaryResponse;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllInstructorHandlerTest {

    @Mock private TenantValidation tenantValidation;
    @Mock private InstructorRepository instructorRepository;
    @InjectMocks private ListAllInstructorHandler handler;

    @Test
    void shouldListActiveInstructorsFromAuthenticatedInstitution() {
        Long institutionId = 7L;
        User user = new User();
        user.setName("Ana Silva");
        user.setEmail("ana@example.com");
        Instructor instructor = new Instructor();
        instructor.setIdInstructor(3L);
        instructor.setUser(user);

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(instructorRepository.findAllByInstitution_IdAndIsActiveTrue(institutionId))
                    .thenReturn(List.of(instructor));

            List<InstructorsSumaryResponse> result = handler.listAllInstructors();

            assertEquals(1, result.size());
            assertEquals(3L, result.get(0).id());
            assertEquals("Ana Silva", result.get(0).name());
            verify(tenantValidation).validateInstitution(institutionId);
            verify(instructorRepository).findAllByInstitution_IdAndIsActiveTrue(institutionId);
        }
    }

    @Test
    void shouldReturnEmptyListWhenInstitutionHasNoActiveInstructors() {
        Long institutionId = 7L;

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getInstitutionId).thenReturn(institutionId);
            when(instructorRepository.findAllByInstitution_IdAndIsActiveTrue(institutionId))
                    .thenReturn(List.of());

            assertEquals(List.of(), handler.listAllInstructors());
        }
    }
}
