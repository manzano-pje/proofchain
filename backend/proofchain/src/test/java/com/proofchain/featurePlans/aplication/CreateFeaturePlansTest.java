package com.proofchain.featurePlans.aplication;

import com.proofchain.featurePlan.aplicattion.command.CreateFeatureCommand;
import com.proofchain.featurePlan.aplicattion.handler.CreateFeatureHandler;
import com.proofchain.featurePlan.domain.model.FeaturePlan;
import com.proofchain.featurePlan.domain.model.enuns.FeaturePlansEnum;
import com.proofchain.featurePlan.infrastructure.repository.FeaturePlansRepository;
import com.proofchain.identities.enums.BillingType;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.plan.Plans;
import com.proofchain.plan.PlansRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateFeaturePlansTest {

    @Mock
    private FeaturePlansRepository featurePlansRepository;
    @Mock
    private PlansRepository plansRepository;
    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private BillingType billingType;
    @Mock
    private FeaturePlansEnum featureEnum;

    @InjectMocks
    private CreateFeatureHandler handler;

    private Institution institution;
    private Plans plans;
    private CreateFeatureCommand command;

    @BeforeEach
    public void setup(){

        // Institution
        institution = new Institution();
        institution.setId(1L);

        plans = new Plans();
        BillingType billingType = BillingType.MANUAL;

        // Plans
        plans.setId(1L);
        plans.setName("Free");
        plans.setPrice(0);
        plans.setDurationDays(7);
        plans.setActive(true);
        plans.setBillingType(billingType);
        plans.setMonthlyCertificateLimit(5);

        // Feature Plans
        featureEnum = FeaturePlansEnum.CERTIFICATES;
        command = new CreateFeatureCommand(
                1L,
                featureEnum,
                5
        );
    }

    @Test
    public void ShouldCreateFeaturePlansWithSucessfuly(){

        when(institutionRepository.existsByIdAndDeletedAtIsNull(institution.getId()))
                .thenReturn(true);

        when(plansRepository.existsById(1L))
                .thenReturn(true);

        when(featurePlansRepository.existsByFeatureAndIdPlan(featureEnum,1L))
                .thenReturn(false);

        handler.handler(command);

        verify(featurePlansRepository, times(1))
                .save(any(FeaturePlan.class));

    }
}
