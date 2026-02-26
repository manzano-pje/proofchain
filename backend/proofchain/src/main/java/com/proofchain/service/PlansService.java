package com.proofchain.service;

import com.proofchain.Dtos.request.PlansRequestDto;
import com.proofchain.identities.Instituition;
import com.proofchain.identities.Plans;
import com.proofchain.repository.PlansRepository;
import com.proofchain.security.SecurityUtils;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
public class PlansService {

    private final PlansRepository plansRepository;
    private final Validations validations;

    public void createPlan(PlansRequestDto plansRequest){

        Optional<Plans> plaansOptional = plansRepository.findByName(plansRequest.getName());

        Plans plans = new Plans();
        plans.setName(plansRequest.getName());
        plans.setPrice(plansRequest.getPrice());
        plans.setActive(plansRequest.isActive());
        plans.setBillingType(plansRequest.getBillingType());
        plans.setMonthlyCertificateLimit(plansRequest.getMonthlyCertificateLimit());
        plans.setCreatedAt(now());
        plans = plansRepository.save(plans);


    }
}
