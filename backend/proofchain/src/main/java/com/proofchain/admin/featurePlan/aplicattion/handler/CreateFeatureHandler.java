package com.proofchain.admin.featurePlan.aplicattion.handler;

import com.proofchain.admin.featurePlan.aplicattion.command.CreateFeatureCommand;
import com.proofchain.admin.featurePlan.domain.model.FeaturePlan;
import com.proofchain.admin.featurePlan.infrastructure.repository.FeatureRepository;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.plan.infrastructure.repository.PlansRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.FeaturePlanMessages;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.exception.messages.PlanMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenatValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateFeatureHandler {

    private FeatureRepository featureRepository;
    private PlansRepository plansRepository;
    private InstitutionRepository institutionRepository;
    private final TenatValidation tenatValidation;

    public void handler(CreateFeatureCommand command){
        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);

        boolean existPlan = plansRepository.existsById(command.getIdPlan());
        if(!existPlan){
            throw new NotFoundException(PlanMessages.PLAN_NOT_FOUND);
        }

        boolean existFeature = featureRepository.existsByFeatureAndIdPlan(command.getFeature(), command.getIdPlan());
        if(existFeature){
            throw new AlreadyExistsException(FeaturePlanMessages.FEATURE_ALREADY_EXISTS);
        }

        FeaturePlan feature = new FeaturePlan();
        feature.setIdPlan(command.getIdPlan());
        feature.setFeature(command.getFeature());
        feature.setQuantity(command.getQuantity());

        featureRepository.save(feature);
    }
}
