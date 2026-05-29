package com.proofchain.featurePlan.aplicattion.handler;

import com.proofchain.featurePlan.aplicattion.command.CreateFeatureCommand;
import com.proofchain.featurePlan.domain.exception.FeatureIsRegisteredException;
import com.proofchain.featurePlan.domain.model.FeaturePlan;
import com.proofchain.featurePlan.infrastructure.repository.FeaturePlansRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.plan.PlanNotFoundException;
import com.proofchain.plan.PlansRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateFeatureHandler {

    private FeaturePlansRepository featurePlansRepository;
    private PlansRepository plansRepository;
    private InstitutionRepository institutionRepository;

    public void handler(CreateFeatureCommand command){
//        Long institutionId = SecurityUtils.getInstitutionId();
//        assert institutionId != null;

        Long institutionId = 1L;
        boolean existInstitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if(!existInstitution){
            throw new InstitutionNotFoundException();
        }

        boolean existPlan = plansRepository.existsById(command.getIdPlan());
        if(!existPlan){
            throw new PlanNotFoundException();
        }

        boolean existFeature = featurePlansRepository.existsByFeatureAndIdPlan(command.getFeature(), command.getIdPlan());
        if(existFeature){
            throw new FeatureIsRegisteredException();
        }

        FeaturePlan feature = new FeaturePlan();
        feature.setIdPlan(command.getIdPlan());
        feature.setFeature(command.getFeature());
        feature.setQuantity(command.getQuantity());

        featurePlansRepository.save(feature);
    }
}
