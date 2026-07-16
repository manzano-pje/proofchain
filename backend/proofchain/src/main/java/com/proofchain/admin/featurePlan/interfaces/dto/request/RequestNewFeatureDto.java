package com.proofchain.admin.featurePlan.interfaces.dto.request;

import com.proofchain.admin.featurePlan.domain.model.enuns.FeaturePlansEnum;
import jakarta.persistence.Enumerated;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestNewFeatureDto {

    @NonNull
    private Long idPlan;
    @NonNull
    @Enumerated
    private FeaturePlansEnum feature;
    @NonNull
    private Integer quantity;
}
