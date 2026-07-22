package com.proofchain.admin.plan.aplication.handler;

import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.plan.aplication.command.PlansCreateCommand;
import com.proofchain.admin.plan.domain.model.Plans;
import com.proofchain.admin.plan.infrastructure.repository.PlansRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.messages.PlanMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenatValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatePlansHandler {

    private final PlansRepository plansRepository;
    private final TenatValidation tenatValidation;

    public void createPlan(PlansCreateCommand command) {

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenatValidation.validateInstitution(institutionId);


        Optional<Plans> plansOptional = plansRepository.findByName(command.getName());
        if (plansOptional.isPresent()) {
            throw new AlreadyExistsException(PlanMessages.PLAN_ALERDY_EXISTS);
        }
        Plans plans = new Plans();
        plans.setName(command.getName());
        plans.setPrice(command.getPrice());
        plans.setDurationDays(command.getDurationDays());
        plans.setBillingType(command.getBillingType());     // MANUAL, RECURRING
        plans.setActive(command.isActive());
        plans.setCreated_at(LocalDate.now());
        plans.setMonthlyCertificateLimit(command.getMonthlyCertificateLimit()); // Limite de certificados mensais
        plansRepository.save(plans);
    }
}
