package com.proofchain.service;

import com.proofchain.Dtos.NewInstituitionRequestDto;
import com.proofchain.configuration.ModelMapperConfig;
import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.identities.Instituition;
import com.proofchain.identities.User;
import com.proofchain.repository.InstituitionRepository;
import com.proofchain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
public class InstituitionService {

    private final InstituitionRepository instituitionRepository;
    private final UserRepository userRepository;
    private final ModelMapperConfig mapper;
    private final PasswordEncoder passwordEncoder;

    public void createInstituition(NewInstituitionRequestDto newInstituitionRequestDto) {

        Optional<Instituition> instituitionOptional = instituitionRepository.findByCnpj(newInstituitionRequestDto.getCnpj());
        if(instituitionOptional.isPresent()){
            throw new BusinessRuleException("Instituição já cadastrada");
        }

        // Valida se usuário já existe
        Optional<User> userOptional = userRepository.findByEmail(newInstituitionRequestDto.getEmail());
        if(userOptional.isPresent()){
            throw new BusinessRuleException("E-mail já cadastrado");
        }

        Instituition instituition = new Instituition();
        instituition.setCnpj(newInstituitionRequestDto.getCnpj());
        instituition.setNameInstituition(newInstituitionRequestDto.getName());
        instituition.setEmailInstituition(newInstituitionRequestDto.getEmail());

        instituition = instituitionRepository.save(instituition);

        Optional<User> userOptional1 = userRepository.findByEmail(newInstituitionRequestDto.getEmail());
        if(userOptional.isPresent()){
            throw new BusinessRuleException("E-mail já cadastrado");
        }

        // Cria usuário
        User user = new User();
        user.setInstituition(instituition);
        user.setPassword(passwordEncoder.encode(newInstituitionRequestDto.getPasword()));
        user.setCreateAt(now());
        user.setActive(true);
        userRepository.save(user);
    }
}
