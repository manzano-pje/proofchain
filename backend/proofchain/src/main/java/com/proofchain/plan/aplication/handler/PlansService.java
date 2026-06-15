package com.proofchain.plan.aplication.handler;

import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.plan.aplication.command.PlansCreateCommand;
import com.proofchain.plan.domain.model.Plans;
import com.proofchain.plan.infrastructure.repository.PlansRepository;
import com.proofchain.shared.exception.BusinessException;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PlansService {

    private final PlansRepository plansRepository;
    private final Validations validations;
    private final InstitutionRepository institutionRepository;

    public void createPlan(PlansCreateCommand command){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        institutionId = 1L;
        boolean exist = institutionRepository.existsById(institutionId);

        Optional<Plans> plansOptional = plansRepository.findByName(command.getName());
        if (plansOptional.isPresent()){
            throw new BusinessException("Plano já cadastrado.");
        }
        Plans plans = new Plans();
        plans.setName(command.getName());
        plans.setPrice(command.getPrice());
        plans.setDurationDays(command.getDurationDays());
        plans.setBillingType(command.getBillingType());     // MANUAL, RECURRING
        plans.setActive(command.isActive());
        plans.setMonthlyCertificateLimit(command.getMonthlyCertificateLimit()); // Limite de certificados mensais
        plansRepository.save(plans);
    }
}
