package com.proofchain.service;

import com.proofchain.Dtos.request.PlansRequestDto;
import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.identities.Plans;
import com.proofchain.repository.PlansRepository;
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

    public void createPlan(PlansRequestDto plansRequestDto){
        // 🔑 Instituição vem do TOKEN, não do request
//        Long institutionId = SecurityUtils.getInstitutionId();
//        Instituition institution = validations.validateInstituition(institutionId);

        Optional<Plans> plansOptional = plansRepository.findByName(plansRequestDto.getName());
        if (plansOptional.isPresent()){
            throw new BusinessRuleException("Plano já cadastrado.");
        }
        Plans plans = new Plans();
        plans.setName(plansRequestDto.getName());
        plans.setPrice(plansRequestDto.getPrice());
        plans.setDurationDays(plansRequestDto.getDurationDays());
        plans.setBillingType(plansRequestDto.getBillingType());     // MANUAL, RECURRING
        plans.setActive(plansRequestDto.isActive());
        plans.setMonthlyCertificateLimit(plansRequestDto.getMonthlyCertificateLimit()); // Limite de certificados mensais
        plans.setCreatedAt(now());
        plansRepository.save(plans);
    }
}
