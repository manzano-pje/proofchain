package com.proofchain.plan.aplication.handler;

import com.proofchain.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.plan.aplication.command.PlansCreateCommand;
import com.proofchain.plan.domain.exception.PlanAlerdyExistException;
import com.proofchain.plan.domain.model.Plans;
import com.proofchain.plan.infrastructure.repository.PlansRepository;
import com.proofchain.shared.exception.InternalServerException;
import com.proofchain.shared.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static java.time.LocalTime.now;

@Service
@AllArgsConstructor
public class PlansCreateHandler {

    private final PlansRepository plansRepository;
    private final InstitutionRepository institutionRepository;

    public void createPlan(PlansCreateCommand command) {

        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        institutionId = 3L;

        if (institutionId == null){
            throw new InternalServerException("Instituição não pode ser nula");
        }
        boolean exist = institutionRepository.existsById(institutionId);
        if (!exist) {
            throw new InstitutionNotFoundException();
        }

        Optional<Plans> plansOptional = plansRepository.findByName(command.getName());
        if (plansOptional.isPresent()) {
            throw new PlanAlerdyExistException();
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
