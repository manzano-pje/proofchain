package com.proofchain.admin.featurePlan.interfaces.controller;

import com.proofchain.admin.featurePlan.aplicattion.handler.*;
import com.proofchain.admin.featurePlan.aplicattion.query.ListAllFeatureQueryHandler;
import com.proofchain.admin.featurePlan.aplicattion.query.GetFeatureQueryHandler;
import com.proofchain.admin.featurePlan.interfaces.dto.request.CreateFeatureRequest;
import com.proofchain.admin.featurePlan.aplicattion.command.CreateFeatureCommand;
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
public class FeatureController {

    // TODO: Documentação
    private CreateFeatureHandler createFeature;
    private DeleteFeatureHandler deleteFeature;
    private UpdateFeatureHandler updateFeature;
    private GetFeatureQueryHandler listOneFeature;
    private ListAllFeatureQueryHandler listallFeature;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createFeature(@Valid @RequestBody CreateFeatureRequest dto){
        CreateFeatureCommand command = new CreateFeatureCommand(dto);
        createFeature.handler(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
