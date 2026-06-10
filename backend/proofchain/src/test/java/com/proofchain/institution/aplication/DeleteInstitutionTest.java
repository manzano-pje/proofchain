package com.proofchain.institution.aplication;

import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.institution.application.handler.DeleteInstitutionHandler;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
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
public class DeleteInstitutionTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private DeleteInstitutionHandler handler;

    private Institution institution;

    @BeforeEach
    void setup() {

        institution = new Institution();

        institution.setId(1L);
        institution.setName("Instituição Teste");
        institution.setCnpj("43419597000116");
        institution.setEmail("instituicao@proofchain.com");
        institution.setDeletedAt(null);
        institution.setCreatedAt(Instant.now());
        institution.setActive(true);
    }

    @Test
    void shouldSoftDeleteInstitutionSuccessfully() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        handler.deleteinstitution(institution.getCnpj());

        verify(institutionRepository, times(1))
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj());

        assertNotNull(institution.getDeletedAt());
    }

    @Test
    void shouldSetDeletedAtWithCurrentTimestamp() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        Instant beforeDelete = Instant.now();

        handler.deleteinstitution(institution.getCnpj());

        Instant afterDelete = Instant.now();

        assertNotNull(institution.getDeletedAt());

        assertTrue(
                !institution.getDeletedAt().isBefore(beforeDelete)
        );

        assertTrue(
                !institution.getDeletedAt().isAfter(afterDelete)
        );
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenInstitutionDoesNotExist() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull("43419597000116"))
                .thenReturn(Optional.empty());

        InstitutionNotFoundException exception =
                assertThrows(
                        InstitutionNotFoundException.class,
                        () -> handler.deleteinstitution("43419597000116")
                );

        assertEquals(
                "Instituição não encontrada.",
                exception.getMessage()
        );

        verify(institutionRepository, times(1))
                .findByCnpjAndDeletedAtIsNull("43419597000116");
    }

    @Test
    void shouldNotChangeOtherInstitutionFieldsDuringDelete() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        String originalName = institution.getName();
        String originalEmail = institution.getEmail();
        String originalCnpj = institution.getCnpj();

        handler.deleteinstitution(institution.getCnpj());

        assertEquals(originalName, institution.getName());
        assertEquals(originalEmail, institution.getEmail());
        assertEquals(originalCnpj, institution.getCnpj());

        assertNotNull(institution.getDeletedAt());
    }

    @Test
    void shouldCallRepositoryOnlyOnce() {

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj()))
                .thenReturn(Optional.of(institution));

        handler.deleteinstitution(institution.getCnpj());

        verify(institutionRepository, times(1))
                .findByCnpjAndDeletedAtIsNull(institution.getCnpj());

        verifyNoMoreInteractions(institutionRepository);
    }

    @Test
    void shouldDeleteMultipleInstitutionsIndependently() {

        Institution institution1 = new Institution();
        institution1.setId(1L);
        institution1.setCnpj("11111111111111");

        Institution institution2 = new Institution();
        institution2.setId(2L);
        institution2.setCnpj("22222222222222");

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull("11111111111111"))
                .thenReturn(Optional.of(institution1));

        when(institutionRepository
                .findByCnpjAndDeletedAtIsNull("22222222222222"))
                .thenReturn(Optional.of(institution2));

        handler.deleteinstitution("11111111111111");
        handler.deleteinstitution("22222222222222");

        assertNotNull(institution1.getDeletedAt());
        assertNotNull(institution2.getDeletedAt());

        assertNotSame(
                institution1.getDeletedAt(),
                institution2.getDeletedAt()
        );
    }
}