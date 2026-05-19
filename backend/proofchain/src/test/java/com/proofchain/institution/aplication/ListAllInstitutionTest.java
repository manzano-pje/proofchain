package com.proofchain.institution.aplication;

import com.proofchain.institution.application.handler.ListAllInstitutionHandler;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ListAllInstitutionTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private ListAllInstitutionHandler handler;

    @BeforeEach
    void setup(){
            
    }


    @Test
    void ShouldListAllInstitutionSucessfuly(){


    }
}
