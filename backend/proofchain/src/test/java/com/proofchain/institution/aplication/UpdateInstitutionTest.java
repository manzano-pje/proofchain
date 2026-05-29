package com.proofchain.institution.aplication;

import com.proofchain.shared.exception.ResourceNotFoundException;
import com.proofchain.institution.application.handler.UpdateInstitutionHandler;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.institution.interfaces.dtos.request.InstitutionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateInstitutionTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private UpdateInstitutionHandler handler;

    private Institution institution;
    private InstitutionRequest request;

    @BeforeEach
    void setup() {

        institution = new Institution();

        institution.setId(1L);
        institution.setName("Instituição Antiga");
        institution.setCnpj("43419597000116");
        institution.setEmail("old@proofchain.com");
        institution.setAddress("Rua Antiga");
        institution.setNumber(10);
        institution.setComplement("Casa");
        institution.setNeighborhood("Centro");
        institution.setCity("Mogi");
        institution.setState("SP");
        institution.setPhone("11999999999");
        institution.setPostalCode("00000000");
        institution.setCreatedAt(Instant.now());
        institution.setDeletedAt(null);
        institution.setActive(true);

        request = new InstitutionRequest(
                "Instituição Nova",
                "Rua Nova",
                99,
                "Sala 9",
                "Quatinga",
                "São Paulo",
                "SP",
                "11888888888",
                "08751655"
        );
    }

    @Test
    void shouldUpdateInstitutionSuccessfully() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        handler.updateinstitution(
                institution.getCnpj(),
                request
        );

        verify(institutionRepository, times(1))
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj());

        verify(institutionRepository, times(1))
                .save(institution);

        assertEquals(request.address(), institution.getAddress());
        assertEquals(request.number(), institution.getNumber());
        assertEquals(request.complement(), institution.getComplement());
        assertEquals(request.neighborhood(), institution.getNeighborhood());
        assertEquals(request.city(), institution.getCity());
        assertEquals(request.state(), institution.getState());
        assertEquals(request.phone(), institution.getPhone());
        assertEquals(request.postalCode(), institution.getPostalCode());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenInstitutionDoesNotExist() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull("43419597000116"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> handler.updateinstitution(
                                "43419597000116",
                                request
                        )
                );

        assertEquals(
                "Instituição não encontrada.",
                exception.getMessage()
        );

        verify(institutionRepository, times(1))
                .findByCnpjAndDeletedAtIsNull("43419597000116");

        verify(institutionRepository, never())
                .save(any());
    }

    @Test
    void shouldPreserveImmutableFieldsDuringUpdate() {

        Long originalId = institution.getId();
        String originalCnpj = institution.getCnpj();
        Instant originalCreatedAt = institution.getCreatedAt();

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        handler.updateinstitution(
                institution.getCnpj(),
                request
        );

        assertEquals(originalId, institution.getId());
        assertEquals(originalCnpj, institution.getCnpj());
        assertEquals(originalCreatedAt, institution.getCreatedAt());
    }

    @Test
    void shouldSaveUpdatedInstitution() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        handler.updateinstitution(
                institution.getCnpj(),
                request
        );

        ArgumentCaptor<Institution> captor =
                ArgumentCaptor.forClass(Institution.class);

        verify(institutionRepository)
                .save(captor.capture());

        Institution savedInstitution = captor.getValue();

        assertEquals(request.address(), savedInstitution.getAddress());
    }

    @Test
    void shouldCallRepositoryMethodsOnlyOnce() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        handler.updateinstitution(
                institution.getCnpj(),
                request
        );

        verify(institutionRepository, times(1))
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj());

        verify(institutionRepository, times(1))
                .save(institution);

        verifyNoMoreInteractions(institutionRepository);
    }

    @Test
    void shouldUpdateInstitutionWithoutChangingDeletedAt() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        handler.updateinstitution(
                institution.getCnpj(),
                request
        );

        assertNull(institution.getDeletedAt());
    }

    @Test
    void shouldUpdateMultipleInstitutionsIndependently() {

        Institution institution2 = new Institution();
        institution2.setId(2L);
        institution2.setCnpj("99999999999999");
        institution2.setName("Instituição 2");

        InstitutionRequest request2 =
                new InstitutionRequest(
                        "Instituição Nova",
                        "Rua XPTO",
                        100,
                        "Sala 10",
                        "Centro",
                        "Rio",
                        "RJ",
                        "11777777777",
                        "99999999"
                );

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution2.getCnpj()))
                .thenReturn(Optional.of(institution2));

        handler.updateinstitution(
                institution.getCnpj(),
                request
        );

        handler.updateinstitution(
                institution2.getCnpj(),
                request2
        );

        assertEquals(
                "Instituição Nova",
                institution.getName(),
                "O nome da instituição não foi atualizado corretamente"
        );

        assertEquals(
                "Instituição Nova",
                institution2.getName()
        );
    }
}