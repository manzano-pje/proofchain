package com.proofchain.featurePlan.interfaces.dto.request;

import com.proofchain.featurePlan.domain.model.enuns.FeaturePlansEnum;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
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
