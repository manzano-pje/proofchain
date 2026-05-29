package com.proofchain.institution.aplication;

import com.proofchain.shared.exception.ResourceNotFoundException;
import com.proofchain.institution.application.handler.ListAllInstitutionHandler;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListAllInstitutionTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private ListAllInstitutionHandler handler;

    private Institution institution1;
    private Institution institution2;

    @BeforeEach
    void setup() {

        institution1 = new Institution();
        institution1.setId(1L);
        institution1.setName("Instituição 1");
        institution1.setCnpj("11111111111111");
        institution1.setEmail("instituicao1@proofchain.com");
        institution1.setAddress("Rua A");
        institution1.setNumber(10);
        institution1.setComplement("Casa");
        institution1.setNeighborhood("Centro");
        institution1.setCity("Mogi");
        institution1.setState("SP");
        institution1.setPhone("11999999999");
        institution1.setPostalCode("00000000");
        institution1.setCreatedAt(Instant.now());
        institution1.setDeletedAt(null);
        institution1.setActive(true);

        institution2 = new Institution();
        institution2.setId(2L);
        institution2.setName("Instituição 2");
        institution2.setCnpj("22222222222222");
        institution2.setEmail("instituicao2@proofchain.com");
        institution2.setAddress("Rua B");
        institution2.setNumber(20);
        institution2.setComplement("Sala 2");
        institution2.setNeighborhood("Quatinga");
        institution2.setCity("São Paulo");
        institution2.setState("SP");
        institution2.setPhone("11888888888");
        institution2.setPostalCode("11111111");
        institution2.setCreatedAt(Instant.now());
        institution2.setDeletedAt(null);
        institution2.setActive(true);
    }

    @Test
    void shouldReturnAllInstitutionsSuccessfully() {

        List<Institution> institutions =
                List.of(institution1, institution2);

        when(institutionRepository.findAllByDeletedAtIsNull())
                .thenReturn(institutions);

        List<InstitutionReturn> result =
                handler.getAllinstitution();

        verify(institutionRepository, times(1))
                .findAllByDeletedAtIsNull();

        assertNotNull(result);

        assertEquals(2, result.size());

        InstitutionReturn first = result.get(0);

        assertEquals(institution1.getId(), first.id());
        assertEquals(institution1.getName(), first.name());
        assertEquals(institution1.getCnpj(), first.cnpj());
        assertEquals(institution1.getEmail(), first.email());
        assertEquals(institution1.getAddress(), first.address());
        assertEquals(institution1.getNumber(), first.number());
        assertEquals(institution1.getComplement(), first.complement());
        assertEquals(institution1.getNeighborhood(), first.neighborhood());
        assertEquals(institution1.getCity(), first.city());
        assertEquals(institution1.getState(), first.state());
        assertEquals(institution1.getPhone(), first.phone());
        assertEquals(institution1.getPostalCode(), first.postalCode());
        assertEquals(institution1.getActive(), first.active());

        InstitutionReturn second = result.get(1);

        assertEquals(institution2.getId(), second.id());
        assertEquals(institution2.getName(), second.name());
        assertEquals(institution2.getCnpj(), second.cnpj());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenListIsEmpty() {

        when(institutionRepository.findAllByDeletedAtIsNull())
                .thenReturn(Collections.emptyList());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> handler.getAllinstitution()
                );

        assertEquals(
                "Não existem instituições cadastradas.",
                exception.getMessage()
        );

        verify(institutionRepository, times(1))
                .findAllByDeletedAtIsNull();
    }

    @Test
    void shouldReturnSingleInstitutionSuccessfully() {

        when(institutionRepository.findAllByDeletedAtIsNull())
                .thenReturn(List.of(institution1));

        List<InstitutionReturn> result =
                handler.getAllinstitution();

        assertNotNull(result);

        assertEquals(1, result.size());

        InstitutionReturn response = result.get(0);

        assertEquals(institution1.getId(), response.id());
        assertEquals(institution1.getName(), response.name());
        assertEquals(institution1.getCnpj(), response.cnpj());
    }

    @Test
    void shouldMaintainListOrder() {

        List<Institution> institutions =
                List.of(institution1, institution2);

        when(institutionRepository.findAllByDeletedAtIsNull())
                .thenReturn(institutions);

        List<InstitutionReturn> result =
                handler.getAllinstitution();

        assertEquals("Instituição 1", result.get(0).name());
        assertEquals("Instituição 2", result.get(1).name());
    }

    @Test
    void shouldReturnLargeInstitutionListSuccessfully() {

        List<Institution> largeList = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {

            Institution institution = new Institution();

            institution.setId((long) i);
            institution.setName("Instituição " + i);
            institution.setCnpj("0000000000000" + i);
            institution.setEmail("email" + i + "@proofchain.com");
            institution.setAddress("Rua " + i);
            institution.setNumber(i);
            institution.setNeighborhood("Bairro " + i);
            institution.setCity("Cidade " + i);
            institution.setState("SP");
            institution.setPhone("1199999999" + i);
            institution.setPostalCode("0000000" + i);
            institution.setCreatedAt(Instant.now());
            institution.setActive(true);

            largeList.add(institution);
        }

        when(institutionRepository.findAllByDeletedAtIsNull())
                .thenReturn(largeList);

        List<InstitutionReturn> result =
                handler.getAllinstitution();

        assertNotNull(result);

        assertEquals(1000, result.size());

        assertEquals(
                "Instituição 1",
                result.get(0).name()
        );

        assertEquals(
                "Instituição 1000",
                result.get(999).name()
        );

        verify(institutionRepository, times(1))
                .findAllByDeletedAtIsNull();
    }

    @Test
    void shouldNotReturnNullObjectsInsideList() {

        when(institutionRepository.findAllByDeletedAtIsNull())
                .thenReturn(List.of(institution1, institution2));

        List<InstitutionReturn> result =
                handler.getAllinstitution();

        assertFalse(result.contains(null));
    }
}