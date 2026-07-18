package com.proofchain.admin.featurePlan.aplicattion.command;

import com.proofchain.admin.featurePlan.domain.model.enuns.FeaturePlansEnum;
import com.proofchain.admin.featurePlan.interfaces.dto.request.CreateFeatureRequest;
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

    public CreateFeatureCommand(CreateFeatureRequest feature){
        this.idPlan = feature.getIdPlan();
        this.feature = feature.getFeature();
        this.quantity = feature.getQuantity();
    }
}
