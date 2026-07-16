package com.proofchain.admin.featurePlan.aplicattion.handler;

import com.proofchain.admin.featurePlan.aplicattion.command.CreateFeatureCommand;
import com.proofchain.admin.featurePlan.domain.exception.FeatureIsRegisteredException;
import com.proofchain.admin.featurePlan.domain.model.FeaturePlan;
import com.proofchain.admin.featurePlan.infrastructure.repository.FeaturePlansRepository;
import com.proofchain.admin.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.plan.domain.exception.PlanNotFoundException;
import com.proofchain.admin.plan.infrastructure.repository.PlansRepository;
//import com.proofchain.security.SecurityUtils;
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
