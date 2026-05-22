package com.proofchain.institution.aplication;

import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.enums.BillingType;
import com.proofchain.identities.enums.StatusSubscription;
import com.proofchain.identities.enums.UserRole;
import com.proofchain.institution.application.handler.CreateInstitutionHandler;
import com.proofchain.institution.domain.exception.InstitutionAlerdyExistException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.institution.interfaces.dtos.request.NewInstitutionRequestDto;
import com.proofchain.plan.Plans;
import com.proofchain.plan.PlansRepository;
import com.proofchain.subscription.SubscriptionRepository;
import com.proofchain.subscription.Subscriptions;
import com.proofchain.user.domain.exception.UserRegisteredException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateInstitutionTest {

    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private PlansRepository plansRepository;

    @InjectMocks
    private CreateInstitutionHandler handler;

    private NewInstitutionRequestDto dto;
    private Plans plan1;
    private Plans plan2;
    private Subscriptions subscriptions;
    private User user;
    private Institution institution;

    @BeforeEach
    void setup(){

        dto = new NewInstitutionRequestDto(
                "Empresa 1",
                "43419597000116",
                "paulo",
                "empresa@proofchain.com.br",
                "admin1234",
                1L
        );

        //Dados Plans1
        plan1 = new Plans();
        plan1.setId(1L);
        plan1.setName("Free");
        plan1.setPrice(0);
        plan1.setDurationDays(10);
        plan1.setActive(true);
        plan1.setBillingType(BillingType.MANUAL);
        plan1.setMonthlyCertificateLimit(5);
        plan1.setCreatedAt(Instant.now());

        //Dados Plans2
        plan2 = new Plans();
        plan2.setId(2L);
        plan2.setName("Free");
        plan2.setPrice(0);
        plan2.setDurationDays(10);
        plan2.setActive(true);
        plan2.setBillingType(BillingType.MANUAL);
        plan2.setMonthlyCertificateLimit(5);
        plan2.setCreatedAt(Instant.now());

        //Dados da lista plans
        List<Plans> plans = new ArrayList<>();
        plans.add(plan1);
        plans.add(plan2);

        // Dados Institution
        institution = new Institution();
        ReflectionTestUtils.setField(institution, "id", 1L);
        institution.setName("Empresa 2");
        institution.setCnpj("43419597000116");
        institution.setEmail("empresa@proofchain.com.br");
        institution.setCreatedAt(Instant.now());
        institution.setDeletedAt(Instant.now());

        // Dados user
        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        user.setName("Bob");
        user.setPassword("123456Admin");
        user.setRole(UserRole.SUPER_ADMIN);
        user.setCreateAt(Instant.now());
        user.setActive(true);
        user.setInstitution(institution);
        user.setEmail("empresa@proofchain.com.br");

        // Dados Subscriptions
        subscriptions = new Subscriptions();
        ReflectionTestUtils.setField(subscriptions, "idSubscription", 1L);
        subscriptions.setInstitution(institution);
        subscriptions.setPlans(plan1);
        subscriptions.setStatus(StatusSubscription.PENDING);
        subscriptions.setBillingType(BillingType.MANUAL);
        subscriptions.setCreatedAt(Instant.now());
    }

    @Nested
    class createInstitution {

        @Test
        void ShouldCreateInstitutionSuccessfully() {

            when(institutionRepository.findByCnpj(dto.getCnpj()))
                    .thenReturn(Optional.empty());

            when(userRepository.existsByEmail(dto.getEmail()))
                    .thenReturn(false);

            when(plansRepository.findById(dto.getIdPlan()))
                    .thenReturn(Optional.of(plan1));

             assertDoesNotThrow(() -> handler.createinstitution(dto));

            verify(institutionRepository).save(any(Institution.class));
            verify(subscriptionRepository).save(any(Subscriptions.class));
        }

        @Test
        void ShouldThrowExceptionWhenInstitutionAlreadyExists(){

            institution.setDeletedAt(null);

            when(institutionRepository.findByCnpj(dto.getCnpj()))
                    .thenReturn(Optional.of(institution));

            assertThrows(
                    InstitutionAlerdyExistException.class,
                    ()-> handler.createinstitution(dto));
        }

        @Test
        void ShouldReactivateInstitutionWhenDeleted(){

            institution.setDeletedAt(Instant.now());

            when(institutionRepository.findByCnpj(dto.getCnpj()))
                    .thenReturn(Optional.of(institution));

            assertDoesNotThrow(() -> handler.createinstitution(dto));
            verify(institutionRepository).save(institution);
            verifyNoInteractions(userRepository);
            verifyNoInteractions(subscriptionRepository);
        }

        @Test
        void ShouldThrowExceptionWhenEmailAlreadyExists(){
            institution.setEmail("empresa@proofchain.com.br");

            when(userRepository.existsByEmail(dto.getEmail()))
                    .thenReturn(true);

            assertThrows(UserRegisteredException.class,
                    ()-> handler.createinstitution(dto));

            verify(institutionRepository, never()).save(any());
            verify(subscriptionRepository, never()).save(any());
        }

        @Test
        void ShouldThrowExceptionWhenPlanNotFound(){

            dto.setPassword("admin1234");
            when(institutionRepository.findByCnpj(dto.getCnpj()))
                    .thenReturn(Optional.empty());

            when(userRepository.existsByEmail(dto.getEmail()))
                    .thenReturn(false);

            when(plansRepository.findById(dto.getIdPlan()))
                    .thenReturn(Optional.of(plan1));

            handler.createinstitution(dto);
            verify(passwordEncoder).encode(dto.getPassword());
        }

        @Test
        void ShouldEncryptPasswordWhenCreatingInstitution(){

            dto.setIdPlan(10L);

            when(institutionRepository.findByCnpj(dto.getCnpj()))
                    .thenReturn(Optional.empty());

            when(userRepository.existsByEmail(dto.getEmail()))
                    .thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                    ()-> handler.createinstitution(dto));

            verify(subscriptionRepository, never()).save(any());
        }
    }
}
