package com.proofchain.plan;

import com.proofchain.course.domain.exception.BusinessRuleException;
import com.proofchain.institution.Institution;
import com.proofchain.plan.dto.request.PlansRequestDto;
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

    public void createPlan(PlansRequestDto plansRequestDto){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Institution institution = validations.validateinstitution(institutionId);

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
