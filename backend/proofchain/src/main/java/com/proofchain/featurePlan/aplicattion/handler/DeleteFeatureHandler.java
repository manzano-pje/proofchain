package com.proofchain.featurePlan.aplicattion.handler;


import com.proofchain.featurePlan.domain.exception.FeatureNotFoundException;
import com.proofchain.featurePlan.domain.model.enuns.FeaturePlansEnum;
import com.proofchain.featurePlan.infrastructure.repository.FeaturePlansRepository;
import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.plan.PlansRepository;
import com.proofchain.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteFeatureHandler {

    private final FeaturePlansRepository featurePlansRepository;
    private final InstitutionRepository institutionRepository;

    public void deleteFeatur(Long idFeature, Long idPlan){
        Long institutionId = SecurityUtils.getInstitutionId();
//        assert institutionId != null;
        institutionId = 1L;

        boolean existIntitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if(!existIntitution){
            throw new InstitutionNotFoundException();
        }

        boolean existFeature = featurePlansRepository.existsByIdAndIdPlan(idFeature, idPlan);
        if(!existFeature){
            throw new FeatureNotFoundException();
        }
        featurePlansRepository.deleteById(idFeature);
    }
}
