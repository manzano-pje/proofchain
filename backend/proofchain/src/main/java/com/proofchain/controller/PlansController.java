package com.proofchain.controller;


import com.proofchain.Dtos.request.PlansRequestDto;
import com.proofchain.service.PlansService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/plans")
public class PlansController{

    private final PlansService plansService;

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<String> createPlan( @RequestBody PlansRequestDto PlansRequestDto){
        plansService.createPlan(PlansRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("caadstrado");
    }

}
