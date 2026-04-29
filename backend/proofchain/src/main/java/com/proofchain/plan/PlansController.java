package com.proofchain.plan;


import com.proofchain.plan.dto.request.PlansRequestDto;
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
public class PlansController {

    private final PlansService plansService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<String> signaturePlan( @RequestBody PlansRequestDto PlansRequestDto){
        plansService.createPlan(PlansRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Plano caadstrado");
    }

}
