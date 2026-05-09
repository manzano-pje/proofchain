package com.proofchain.institution;

import com.proofchain.course.domain.exception.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.enums.BillingType;
import com.proofchain.identities.enums.StatusSubscription;
import com.proofchain.identities.enums.UserRole;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.interfaces.dtos.request.InstitutionRequest;
import com.proofchain.institution.interfaces.dtos.request.NewInstitutionRequestDto;
import com.proofchain.institution.interfaces.dtos.response.InstitutionReturn;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.plan.Plans;
import com.proofchain.plan.PlansRepository;
import com.proofchain.plataform.domain.ModelMapperConfig;
import com.proofchain.subscription.SubscriptionRepository;
import com.proofchain.subscription.Subscriptions;
import com.proofchain.user.User;
import com.proofchain.user.UserRepository;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final ModelMapperConfig mapper;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionRepository subscriptionRepository;
    private final Validations validations;
    private final PlansRepository plansRepository;

    public void createinstitution(NewInstitutionRequestDto newinstitutionRequestDto) {
        if(newinstitutionRequestDto.getCnpj() == null || (newinstitutionRequestDto.getCnpj().length() != 14)){
            throw new BusinessRuleException("CNPJ inválido");
        }
        if(newinstitutionRequestDto.getName() == null || newinstitutionRequestDto.getName().length() < 5){
            throw new BusinessRuleException("Nome inválido");
        }
        if(newinstitutionRequestDto.getEmail() == null){
            throw new BusinessRuleException("E-mail inválido");
        }

        // Validação da instituição
        Optional<Institution> institutionOptional = institutionRepository.findByCnpj(newinstitutionRequestDto.getCnpj());

        // Valida de instituição está inativa. Se estiver, ativa
        if(!institutionOptional.isEmpty() && institutionOptional.get().getDeletedAt() == null) {
            throw new BusinessRuleException("Instituição já cadastrado");
        }
        if(!institutionOptional.isEmpty() && institutionOptional.get().getDeletedAt() != null) {
            Institution institution = institutionOptional.get();
            institution.setDeletedAt(null);
            institution.setActive(true);
            institutionRepository.save(institution);
            return;
        }

        // Valida se usuário já existe
        Optional<User> userOptional = userRepository.findByEmail(newinstitutionRequestDto.getEmail());
        if(userOptional.isPresent()){
            throw new BusinessRuleException("E-mail já cadastrado");
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
        int idPlan = newinstitutionRequestDto.getIdPlan();

        Optional<Plans> plans = plansRepository.findById(idPlan);
        if (plans.isEmpty()) {
            throw new ResourceNotFoundException("Plano não encontrado no banco de dados.");
        }
        Subscriptions subscription = new Subscriptions();

        switch(idPlan){
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
                throw new ResourceNotFoundException("Plano inválido.");
        }

        subscription.setInstitution(institution);
        subscription.setPlans(plans.get());
        subscription.setStatus(StatusSubscription.PENDING);
        subscription.setBillingType(billingType);
        subscription.setCurrentPeriodStarts(null);
        subscription.setCurrentPeriodEnd(null);
        subscription.setCreatedAt(Instant.now());

        institutionRepository.save(institution);
        subscriptionRepository.save(subscription);
    }

    // Somente para administrador da plataforma
    public List<InstitutionReturn> getAllinstitution(){

        List<Institution> institutionList = institutionRepository.findAllByDeletedAtIsNull();
        if(institutionList.isEmpty()){
            throw new ResourceNotFoundException("Não existem instituições cadastradas.");
        }

            return institutionList.stream()
                .map(InstitutionReturn::from)
                .collect(Collectors.toList());
    }

    public InstitutionReturn getOneinstitution(String cnpj){

//        Long institutionId = SecurityUtils.getInstitutionId();
//        validations.validateinstitution(institutionId);

        Optional<Institution> institutionOptional = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj);
        if(institutionOptional.isEmpty()){
            throw new ResourceNotFoundException("Instituição não encontrada.");
        }
        return InstitutionReturn.from(institutionOptional.get());
    }

    public void updateinstitution(String cnpj, InstitutionRequest institutionRequest){

//        Long institutionId = SecurityUtils.getInstitutionId();
//        validations.validateinstitution(institutionId);

        Institution institution = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada."));

        institution.updateFrom(institutionRequest);
        institutionRepository.save(institution);
    }

    @Transactional
    public void deleteinstitution(String cnpj){

//        Long institutionId = SecurityUtils.getInstitutionId();
//        validations.validateinstitution(institutionId);

        Institution institution = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj)
         .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada."));
        institution.setDeletedAt(Instant.now());

    }


}
