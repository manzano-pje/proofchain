package com.proofchain.user.application;

import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.user.applications.handler.DeleteUserHandler;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.function.Try;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteUserHandlerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private DeleteUserHandler deleteUserHandler;

    private Institution institution;

    @BeforeEach
    public void setup(){
        institution = new Institution();
        institution.setId(1L);

    }

    @Test
    public void SoutchDeleteUserSucessfuly(){

        try(MockedStatic<SecurityUtils> security =
                    mockStatic(SecurityUtils.class)){

            security.when(() -> SecurityUtils.getInstitutionId())
                    .thenReturn(1L);

            when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                    .thenReturn(true);

            when(userRepository.existsByIdAndInstitutionId(1L, 1L))
                    .thenReturn(true);

            deleteUserHandler.deleteUSer(1L);

            verify(userRepository, times(1))
                    .deleteById(1L);
        }
    }
}
