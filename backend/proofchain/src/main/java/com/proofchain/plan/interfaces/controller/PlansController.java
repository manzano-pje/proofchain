package com.proofchain.plan.interfaces.controller;


import com.proofchain.plan.aplication.command.PlansCreateCommand;
import com.proofchain.plan.aplication.handler.PlansService;
import com.proofchain.plan.interfaces.dto.request.PlansRequestDto;
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

    private final PlansService plansService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<String> signaturePlan( @Valid @RequestBody PlansRequestDto dto){
        PlansCreateCommand command = new PlansCreateCommand(dto);
        plansService.createPlan(command);
        return ResponseEntity.status(HttpStatus.CREATED).body("Plano caadstrado");
    }

}
