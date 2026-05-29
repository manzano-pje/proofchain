package com.proofchain.featurePlan.interfaces.controller;

import com.proofchain.featurePlan.interfaces.dto.request.RequestNewFeatureDto;
import com.proofchain.featurePlan.aplicattion.command.CreateFeatureCommand;
import com.proofchain.featurePlan.aplicattion.handler.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/feature/")
public class FeaturePlanController {

    private CreateFeatureHandler createFeature;
    private DeleteFeatureHandler deleteFeature;
    private UpdateFeatureHandler updateFeature;
    private ListOneFeatureHandler listOneFeature;
    private ListAllFeatureHandler listallFeature;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createFeature(@Valid @RequestBody RequestNewFeatureDto dto){
        CreateFeatureCommand command = new CreateFeatureCommand(dto);
        createFeature.handler(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
