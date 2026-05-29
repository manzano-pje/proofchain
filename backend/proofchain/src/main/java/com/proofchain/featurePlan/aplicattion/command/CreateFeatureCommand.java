package com.proofchain.featurePlan.aplicattion.command;

import com.proofchain.featurePlan.domain.model.enuns.FeaturePlansEnum;
import com.proofchain.featurePlan.interfaces.dto.request.RequestNewFeatureDto;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateFeatureCommand {

    private Long idPlan;
    @Enumerated
    private FeaturePlansEnum feature;
    private Integer quantity;

    public CreateFeatureCommand(RequestNewFeatureDto feature){
        this.idPlan = feature.getIdPlan();
        this.feature = feature.getFeature();
        this.quantity = feature.getQuantity();
    }
}
