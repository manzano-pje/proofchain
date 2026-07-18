package com.proofchain.admin.featurePlan.aplicattion.handler;


import com.proofchain.admin.featurePlan.infrastructure.repository.FeatureRepository;
import com.proofchain.admin.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteFeatureHandler {

    private final FeatureRepository featureRepository;
    private final InstitutionRepository institutionRepository;

    public void deleteFeatur(Long idFeature, Long idPlan){
//        Long institutionId = SecurityUtils.getInstitutionId();
//        assert institutionId != null;
        Long institutionId = 1L;

        boolean existIntitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
        if(!existIntitution){
            throw new InstitutionNotFoundException();
        }

        boolean existFeature = featureRepository.existsByIdFeatureAndIdPlan(idFeature, idPlan);
        if(!existFeature){
            throw new NotFoundException("Feature não existe");
        }
        featureRepository.deleteById(idFeature);
    }
}
