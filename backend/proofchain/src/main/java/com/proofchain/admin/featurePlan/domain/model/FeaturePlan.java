package com.proofchain.admin.featurePlan.domain.model;

import com.proofchain.admin.featurePlan.domain.model.enuns.FeaturePlansEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table (name = "tb_feature_plans")
public class FeaturePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFeature;
    private Long idPlan;
    @Enumerated
    private FeaturePlansEnum feature;
    private Integer quantity;
}
