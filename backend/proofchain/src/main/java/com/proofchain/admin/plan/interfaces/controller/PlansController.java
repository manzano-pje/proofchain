package com.proofchain.admin.plan.interfaces.controller;


import com.proofchain.admin.plan.aplication.command.PlansCreateCommand;
import com.proofchain.admin.plan.aplication.handler.PlansCreateHandler;
import com.proofchain.admin.plan.interfaces.dto.request.PlansRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/plans")
public class PlansController {

    private final PlansCreateHandler plansCreateHandler;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<String> signaturePlan( @Valid @RequestBody PlansRequestDto dto){
        PlansCreateCommand command = new PlansCreateCommand(dto);
        plansCreateHandler.createPlan(command);
        return ResponseEntity.status(HttpStatus.CREATED).body("Plano caadstrado");
    }

}
