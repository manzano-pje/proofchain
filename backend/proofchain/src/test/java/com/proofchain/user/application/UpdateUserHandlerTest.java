package com.proofchain.user.application;

import com.proofchain.user.domain.model.UserRole;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.user.applications.command.UpdateUserCommand;
import com.proofchain.user.applications.handler.UpdateUserHandler;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateUserHandlerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private UpdateUserHandler update;

    private UpdateUserCommand command;
    private Institution institution;
    private User user;

    @BeforeEach
    public void setup(){

        institution = new Institution();
        institution.setId(1L);

        user = new User();
        user.setId(1L);
        user.setName("Paulo");
        user.setEmail("teste@email.com");
        user.setPassword("123456asdf");
        user.setRole(UserRole.ADMIN);
        user.setActive(true);

        command = new UpdateUserCommand(
                "João",
                UserRole.ADMIN,
                true
        );
    }

    @Test
    public void SoutchUpdateUserSucefully(){

        user.setId(1L);
        when(institutionRepository.existsByIdAndDeletedAtIsNull(1L))
                .thenReturn(true);

        when(userRepository.findByIdAndInstitution_Id(1L, 1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserReturn response = update.updateUser(user.getId(), command);

        verify(userRepository, times(1))
                .save(any(User.class));

        assertNotNull(response);
        assertEquals(command.getName(), response.name());
    }
}
