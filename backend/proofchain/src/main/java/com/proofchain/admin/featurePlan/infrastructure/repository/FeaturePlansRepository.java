package com.proofchain.admin.featurePlan.infrastructure.repository;

import com.proofchain.admin.featurePlan.domain.model.FeaturePlan;
import com.proofchain.admin.featurePlan.domain.model.enuns.FeaturePlansEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturePlansRepository extends JpaRepository<FeaturePlan, Long> {

    boolean existsByFeatureAndIdPlan(FeaturePlansEnum feature, Long idPlan);
    boolean existsByIdFeatureAndIdPlan(Long idFeatrue, Long idPlan);
}
