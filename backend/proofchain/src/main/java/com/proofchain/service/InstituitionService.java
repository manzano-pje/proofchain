package com.proofchain.service;

import com.proofchain.Dtos.request.InstituitionRequestDto;
import com.proofchain.Dtos.response.InstituitionReturnDto;
import com.proofchain.Dtos.request.NewInstituitionRequestDto;
import com.proofchain.configuration.ModelMapperConfig;
import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.Instituition;
import com.proofchain.identities.Plans;
import com.proofchain.identities.Subscriptions;
import com.proofchain.identities.User;
import com.proofchain.identities.enums.BillingType;
import com.proofchain.identities.enums.StatusSubscription;
import com.proofchain.identities.enums.UserRole;
import com.proofchain.repository.InstituitionRepository;
import com.proofchain.repository.PlansRepository;
import com.proofchain.repository.SubscriptionRepository;
import com.proofchain.repository.UserRepository;
import com.proofchain.security.SecurityUtils;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
public class InstituitionService {

    private final InstituitionRepository instituitionRepository;
    private final UserRepository userRepository;
    private final ModelMapperConfig mapper;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionRepository subscriptionRepository;
    private final Validations validations;
    private final PlansRepository plansRepository;;

    public void createInstituition(NewInstituitionRequestDto newInstituitionRequestDto) {
        if(newInstituitionRequestDto.getCnpj() == null || (newInstituitionRequestDto.getCnpj().length() != 14)){
            throw new BusinessRuleException("CNPJ inválido");
        }
        if(newInstituitionRequestDto.getName() == null || newInstituitionRequestDto.getName().length() < 5){
            throw new BusinessRuleException("Nome inválido");
        }
        if(newInstituitionRequestDto.getEmail() == null){
            throw new BusinessRuleException("E-mail inválido");
        }

        Optional<Instituition> instituitionOptional = instituitionRepository.findByCnpj(newInstituitionRequestDto.getCnpj());
        if(instituitionOptional.isPresent()){
            throw new BusinessRuleException("Instituição já cadastrada");
        }

        // Valida se usuário já existe
        Optional<User> userOptional = userRepository.findByEmail(newInstituitionRequestDto.getEmail());
        if(userOptional.isPresent()){
            throw new BusinessRuleException("E-mail já cadastrado");
        }

        ///////// CRIA INSTITUIÇÃO /////////
        Instituition instituition = new Instituition();
        instituition.setCnpj(newInstituitionRequestDto.getCnpj());
        instituition.setName(newInstituitionRequestDto.getName());
        instituition.setEmail(newInstituitionRequestDto.getEmail());
        instituition.setCreatedAt(now());

        ///////// CRIA USUÁRIO /////////
        User user = new User();
        user.setName(newInstituitionRequestDto.getName());
        user.setEmail(newInstituitionRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(newInstituitionRequestDto.getPassword()));
        user.setRole((UserRole.ROLE_SUPER_ADMIN));
        user.setCreateAt(now());
        user.setActive(true);
        user.setInstituition(instituition);

        instituition.getListUsers().add(user);

        ///////// CRIA ASSINATURA /////////
        BillingType billingType = null;
        Instant periodStart ;
        Instant periodEnd ;
        Instant nextBilling;
        Long idPlan = newInstituitionRequestDto.getIdPlan();
        Optional<Plans> plans = plansRepository.findById(idPlan);
        Subscriptions subscription = new Subscriptions();

        switch(idPlan.intValue()){
            case 1:
                billingType = subscription.getBillingType().MANUAL;
                periodStart = now();
                periodEnd = now().plus(7, ChronoUnit.DAYS);
                break;
            case 2:
                billingType = subscription.getBillingType().MANUAL;
                periodStart = now();
                periodEnd = now().plus(15, ChronoUnit.DAYS);
                break;
            case 3:
                billingType = subscription.getBillingType().RECURRING;
                periodStart = now();
                periodEnd = now().plus(30, ChronoUnit.DAYS);
                nextBilling =  now().plus(30, ChronoUnit.DAYS);
                break;
            case 4:
                billingType = subscription.getBillingType().RECURRING;
                periodStart = now();
                periodEnd = now().plus(30, ChronoUnit.DAYS);
                nextBilling =  now().plus(30, ChronoUnit.DAYS);
                break;
            default:
                throw new ResourceNotFoundException("Plano inválido.");
        }

        subscription.setInstituition(instituition);
        subscription.setPlans(plans.get());
        subscription.setStatusSubscription(StatusSubscription.PENDING);
        subscription.setBillingType(billingType);
        subscription.setCurrentPeriodStarts(null);
        subscription.setCurrentPeriodEnd(null);
        subscription.setCreatedAt(now());

        instituitionRepository.save(instituition);
        subscriptionRepository.save(subscription);

    }

    public void updateInstituition(String cnpj, InstituitionRequestDto instituitionRequestDto){

        Long institutionId = SecurityUtils.getInstitutionId();
        validations.validateInstituition(institutionId);

        Optional<Instituition> instituitionOptional = instituitionRepository.findByCnpj(cnpj);
        if(instituitionOptional.isEmpty()){
            throw new ResourceNotFoundException("Instituição não encontrada.");
        }

        Instituition instituition = new Instituition();
        instituition.setId (instituitionOptional.get().getId());
        instituition.setAddress (instituitionRequestDto.address());
        instituition.setNumber (instituitionRequestDto.number());
        instituition.setComplement (instituitionRequestDto.complement());
        instituition.setNeighborhood (instituitionRequestDto.neighborhood());
        instituition.setCity (instituitionRequestDto.city());
        instituition.setState (instituitionRequestDto.state());
        instituition.setPostalCode (instituitionRequestDto.postalCode());
        instituition.setPhone (instituitionRequestDto.phone());

        instituitionRepository.save(instituition);
    }

    public InstituitionReturnDto getOneInstituition(String cnpj){

        Long institutionId = SecurityUtils.getInstitutionId();
        validations.validateInstituition(institutionId);

        Optional<Instituition> instituitionOptional = instituitionRepository.findByCnpj(cnpj);
        if(instituitionOptional.isEmpty()){
            throw new ResourceNotFoundException("Instituição não encontrada.");
        }
        InstituitionReturnDto instituition = new InstituitionReturnDto();
        instituition = mapper.modelMapper().map(instituitionOptional, InstituitionReturnDto.class);
        return instituition;
    }

    public void deleteInstituition(String cnpj){

        Long institutionId = SecurityUtils.getInstitutionId();
        validations.validateInstituition(institutionId);

        Optional<Instituition> instituitionOptional = instituitionRepository.findByCnpj(cnpj);
        if(instituitionOptional.isEmpty()){
            throw new ResourceNotFoundException("Instituição não encontrada.");
        }
        instituitionRepository.deleteByCnpj(cnpj);
    }

    // Somente para administrador da plataforma
    public List<InstituitionReturnDto> getAllInstituition(){

        List<Instituition> instituitionList = instituitionRepository.findAll();
        if(instituitionList.isEmpty()){
            throw new ResourceNotFoundException("Não existem instituições cadastradas.");
        }

        return instituitionList.stream()
                .map(InstituitionReturnDto::new)
                .collect(Collectors.toList());
    }
}
