package com.proofchain.featurePlan;

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
    private String feature;
    private Integer quantity;

}
