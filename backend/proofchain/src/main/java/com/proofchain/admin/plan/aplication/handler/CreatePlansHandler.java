package com.proofchain.admin.plan.aplication.handler;

import com.proofchain.admin.institution.domain.exception.InstitutionNotFoundException;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.plan.aplication.command.PlansCreateCommand;
import com.proofchain.admin.plan.domain.exception.PlanAlerdyExistException;
import com.proofchain.admin.plan.domain.model.Plans;
import com.proofchain.admin.plan.infrastructure.repository.PlansRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.InternalServerException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.exception.messages.PlanMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreatePlansHandler {

    private final PlansRepository plansRepository;
    private final InstitutionRepository institutionRepository;

    public void createPlan(PlansCreateCommand command) {

        // 🔑 Instituição vem do TOKEN, não do request
       // Long institutionId = SecurityUtils.getInstitutionId();
        Long institutionId = 1L;

        if (institutionId == null){
            throw new InternalServerException("Instituição não pode ser nula");
        }
        boolean exist = institutionRepository.existsById(institutionId);
        if (!exist) {
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }

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
