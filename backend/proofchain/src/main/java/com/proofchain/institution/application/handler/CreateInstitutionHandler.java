package com.proofchain.institution.application.handler;

import com.proofchain.identities.enums.BillingType;
import com.proofchain.identities.enums.StatusSubscription;
import com.proofchain.identities.enums.UserRole;
import com.proofchain.institution.domain.exception.InstitutionAlerdyExistException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.institution.interfaces.dtos.request.NewInstitutionRequestDto;
import com.proofchain.plan.PlanNotFoundException;
import com.proofchain.plan.Plans;
import com.proofchain.plan.PlansRepository;
import com.proofchain.shared.exception.BusinessException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.subscription.SubscriptionRepository;
import com.proofchain.subscription.Subscriptions;
import com.proofchain.user.domain.exception.UserRegisteredException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CreateInstitutionHandler {

    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionRepository subscriptionRepository;
    private final PlansRepository plansRepository;

    public void createinstitution(NewInstitutionRequestDto newinstitutionRequestDto) {
        if(newinstitutionRequestDto.getCnpj() == null || (newinstitutionRequestDto.getCnpj().length() != 14)){
            throw new BusinessException("CNPJ inválido");
        }
        if(newinstitutionRequestDto.getName() == null || newinstitutionRequestDto.getName().length() < 5){
            throw new BusinessException("Nome inválido");
        }
        if(newinstitutionRequestDto.getEmail() == null){
            throw new BusinessException("E-mail inválido");
        }

        // Validação da instituição
        Optional<Institution> institutionOptional = institutionRepository.findByCnpj(newinstitutionRequestDto.getCnpj());

        // Valida de instituição está inativa. Se estiver, ativa
        if(institutionOptional.isPresent() && institutionOptional.get().getDeletedAt() == null) {
            throw new InstitutionAlerdyExistException();
        }
        if(institutionOptional.isPresent() && institutionOptional.get().getDeletedAt() != null) {
            Institution institution = institutionOptional.get();
            institution.setDeletedAt(null);
            institution.setActive(true);
            institutionRepository.save(institution);
            return;
        }

        // Valida se usuário já existe
       boolean existUser = userRepository.existsByEmail(newinstitutionRequestDto.getEmail());

        if(existUser){
            throw new UserRegisteredException();
        }

        ///////// CRIA INSTITUIÇÃO /////////
        Institution institution = new Institution();
        institution.setCnpj(newinstitutionRequestDto.getCnpj());
        institution.setName(newinstitutionRequestDto.getName());
        institution.setEmail(newinstitutionRequestDto.getEmail());
        institution.setCreatedAt(Instant.now());

        ///////// CRIA USUÁRIO /////////
        User user = new User();
        user.setName(newinstitutionRequestDto.getName());
        user.setEmail(newinstitutionRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(newinstitutionRequestDto.getPassword()));
        user.setRole((UserRole.SUPER_ADMIN));
        user.setCreateAt(Instant.now());
        user.setActive(true);
        user.setInstitution(institution);

        institution.getListUsers().add(user);

        ///////// CRIA ASSINATURA /////////
        BillingType billingType ;
        Instant periodStart ;
        Instant periodEnd ;
        Instant nextBilling;
        Long idPlan = newinstitutionRequestDto.getIdPlan();

        Optional<Plans> plans = plansRepository.findById(idPlan);

        if (plans.isEmpty()) {
            throw new PlanNotFoundException();
        }
        Subscriptions subscription = new Subscriptions();

        switch(Math.toIntExact(idPlan)){
            case 1:
                billingType = subscription.getBillingType().MANUAL;
                periodStart = Instant.now();
                periodEnd = Instant.now().plus(7, ChronoUnit.DAYS);
                break;
            case 2:
                billingType = subscription.getBillingType().MANUAL;
                periodStart = Instant.now();
                periodEnd = Instant.now().plus(15, ChronoUnit.DAYS);
                break;
            case 3:
                billingType = subscription.getBillingType().RECURRING;
                periodStart = Instant.now();
                periodEnd = Instant.now().plus(30, ChronoUnit.DAYS);
                nextBilling =  Instant.now().plus(30, ChronoUnit.DAYS);
                break;
            case 4:
                billingType = subscription.getBillingType().RECURRING;
                periodStart = Instant.now();
                periodEnd = Instant.now().plus(30, ChronoUnit.DAYS);
                nextBilling =  Instant.now().plus(30, ChronoUnit.DAYS);
                break;
            default:
                throw new PlanNotFoundException();
        }

        subscription.setInstitution(institution);
        subscription.setPlans(plans.get());
        subscription.setStatus(StatusSubscription.PENDING);
        subscription.setBillingType(billingType);
        subscription.setCurrentPeriodStarts(null);
        subscription.setCurrentPeriodEnd(null);

        institutionRepository.save(institution);
        subscriptionRepository.save(subscription);
    }
}
