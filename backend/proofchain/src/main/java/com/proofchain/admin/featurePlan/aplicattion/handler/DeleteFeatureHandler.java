package com.proofchain.admin.featurePlan.aplicattion.handler;


import com.proofchain.admin.featurePlan.infrastructure.repository.FeatureRepository;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.FeaturePlanMessages;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteFeatureHandler {

    private final FeatureRepository featureRepository;
    private final InstitutionRepository institutionRepository;

    public void deleteFeatur(Long idFeature, Long idPlan){
        Long institutionId = SecurityUtils.getInstitutionId();
        assert institutionId != null;

        boolean existIntitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if(!existIntitution){
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }

        boolean existFeature = featureRepository.existsByIdFeatureAndIdPlan(idFeature, idPlan);
        if(!existFeature){
            throw new NotFoundException(FeaturePlanMessages.FEATURE_NOT_FOUND);
        }
        featureRepository.deleteById(idFeature);
    }
}
