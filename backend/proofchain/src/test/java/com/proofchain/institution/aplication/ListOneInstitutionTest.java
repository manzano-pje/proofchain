package com.proofchain.institution.aplication;

import com.proofchain.shared.exception.ResourceNotFoundException;
import com.proofchain.institution.application.handler.ListOneInstitutionHandler;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.institution.interfaces.dtos.response.InstitutionReturn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListOneInstitutionTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private ListOneInstitutionHandler handler;

    private Institution institution2;

    @BeforeEach
    void setup() {

        institution2 = new Institution();
        institution2.setId(2L);
        institution2.setName("Instituição 2");
        institution2.setCnpj("43419597000116");
        institution2.setEmail("instituicao2@proofchain.com.br");
        institution2.setAddress("Rua Maximiniano José de Araújo");
        institution2.setNumber(71);
        institution2.setComplement("Casa 2");
        institution2.setNeighborhood("Quatinga");
        institution2.setCity("São Paulo");
        institution2.setState("São Paulo");
        institution2.setPhone("11983050657");
        institution2.setPostalCode("08751655");
        institution2.setCreatedAt(Instant.now());
        institution2.setDeletedAt(null);
        institution2.setActive(true);
    }

    @Test
    void shouldReturnInstitutionSuccessfully() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution2.getCnpj()))
                .thenReturn(Optional.of(institution2));

        InstitutionReturn result =
                handler.getOneinstitution(institution2.getCnpj());

        verify(institutionRepository, times(1))
                .findByCnpjAndDeletedAtIsNull(institution2.getCnpj());

        assertNotNull(result);

        assertEquals(institution2.getId(), result.id());
        assertEquals(institution2.getName(), result.name());
        assertEquals(institution2.getCnpj(), result.cnpj());
        assertEquals(institution2.getEmail(), result.email());
        assertEquals(institution2.getAddress(), result.address());
        assertEquals(institution2.getNumber(), result.number());
        assertEquals(institution2.getComplement(), result.complement());
        assertEquals(institution2.getNeighborhood(), result.neighborhood());
        assertEquals(institution2.getCity(), result.city());
        assertEquals(institution2.getState(), result.state());
        assertEquals(institution2.getPhone(), result.phone());
        assertEquals(institution2.getPostalCode(), result.postalCode());
        assertEquals(institution2.getCreatedAt(), result.createdAt());
        assertEquals(institution2.getActive(), result.active());

        assertNull(result.DeletedAt());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenInstitutionDoesNotExist() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull("43419597000116"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> handler.getOneinstitution("43419597000116")
        );

        assertEquals(
                "Instituição não encontrada.",
                exception.getMessage()
        );

        verify(institutionRepository, times(1))
                .findByCnpjAndDeletedAtIsNull("43419597000116");
    }
}