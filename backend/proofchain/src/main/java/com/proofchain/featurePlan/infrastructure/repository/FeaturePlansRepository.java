package com.proofchain.featurePlan.infrastructure.repository;

import com.proofchain.featurePlan.domain.model.FeaturePlan;
import com.proofchain.featurePlan.domain.model.enuns.FeaturePlansEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturePlansRepository extends JpaRepository<FeaturePlan, Long> {

    boolean existsByFeatureAndIdPlan(FeaturePlansEnum feature, Long idPlan);
}
