package com.proofchain.service;

import com.proofchain.Dtos.InstituitionRequestDto;
import com.proofchain.Dtos.InstituitionReturnDto;
import com.proofchain.Dtos.NewInstituitionRequestDto;
import com.proofchain.configuration.ModelMapperConfig;
import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.Instituition;
import com.proofchain.identities.User;
import com.proofchain.repository.InstituitionRepository;
import com.proofchain.repository.UserRepository;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void createInstituition(NewInstituitionRequestDto newInstituitionRequestDto) {
        System.out.println("Entrou no service: " + newInstituitionRequestDto.toString());
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

        Optional<User> userOptional1 = userRepository.findByEmail(newInstituitionRequestDto.getEmail());
        if(userOptional.isPresent()){
            throw new BusinessRuleException("E-mail já cadastrado");
        }

        // Cria usuário
        User user = new User();
        user.setInstituition(instituition);
        user.setPassword(passwordEncoder.encode(newInstituitionRequestDto.getPassword()));
        user.setCreateAt(now());
        user.setActive(true);
        userRepository.save(user);

        Instituition instituition = new Instituition();
        instituition.setCnpj(newInstituitionRequestDto.getCnpj());
        instituition.setUserInstituition(newInstituitionRequestDto.getUserName());
        instituition.setNameInstituition(newInstituitionRequestDto.getName());
        instituition.setEmailInstituition(newInstituitionRequestDto.getEmail());

        instituition = instituitionRepository.save(instituition);


    }

    public void updateInstituition(String cnpj, InstituitionRequestDto instituitionRequestDto){
        Optional<Instituition> instituitionOptional = instituitionRepository.findByCnpj(cnpj);
        if(instituitionOptional.isEmpty()){
            throw new ResourceNotFoundException("Instituição não encontrada.");
        }

        Instituition instituition = new Instituition();
        instituition.setIdInstituition(instituitionOptional.get().getIdInstituition());
        instituition.setAddressInstituition(instituitionRequestDto.addressInstituition());
        instituition.setNumberInstituition(instituitionRequestDto.numberInstituition());
        instituition.setComplementInstituition(instituitionRequestDto.complementInstituition());
        instituition.setNeighborhoodInstituition(instituitionRequestDto.neighborhoodInstituition());
        instituition.setCityInstituition(instituition.getCityInstituition());
        instituition.setStateInstituition(instituitionRequestDto.stateInstituition());
        instituition.setPostalCodeInstituition(instituition.getPostalCodeInstituition());
        instituition.setPhoneInstituition(instituition.getPhoneInstituition());

        instituitionRepository.save(instituition);
    }

    public List<InstituitionReturnDto> getAllInstituition(){
        List<Instituition> instituitionList = instituitionRepository.findAll();
        if(instituitionList.isEmpty()){
            throw new ResourceNotFoundException("Não existem instituições cadastradas.");
        }

        return instituitionList.stream()
                .map(InstituitionReturnDto::new)
                .collect(Collectors.toList());
    }

    public InstituitionReturnDto getOneInstituition(String cnpj){
        Optional<Instituition> instituitionOptional = instituitionRepository.findByCnpj(cnpj);
        if(instituitionOptional.isEmpty()){
            throw new ResourceNotFoundException("Instituição não encontrada.");
        }
        InstituitionReturnDto instituition = new InstituitionReturnDto();
        instituition = mapper.modelMapper().map(instituitionOptional, InstituitionReturnDto.class);
        return instituition;
    }

    public void deleteInstituition(String cnpj){
        Optional<Instituition> instituitionOptional = instituitionRepository.findByCnpj(cnpj);
        if(instituitionOptional.isEmpty()){
            throw new ResourceNotFoundException("Instituição não encontrada.");
        }
        instituitionRepository.deleteByCnpj(cnpj);
    }
}
