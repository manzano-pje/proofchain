package com.proofchain.service;

import com.proofchain.Dtos.UserRequestDto;
import com.proofchain.Dtos.UserReturnDto;
import com.proofchain.configuration.ModelMapperConfig;
import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.Instituition;
import com.proofchain.identities.User;
import com.proofchain.repository.InstituitionRepository;
import com.proofchain.repository.UserRepository;
import com.proofchain.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InstituitionRepository instituitionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapperConfig mapper;

    public void createUser(UserRequestDto newUser) {
        // 🔑 Instituição vem do TOKEN, não do request
        // Valida se instituição existe
        // ####### ALTERAR PARA VALIDAR POR CNPJ ########
        Optional<Instituition> institutionOptional = instituitionRepository.findByidInstituition(SecurityUtils.getInstitutionId());
        if(institutionOptional.isEmpty()){
            throw new ResourceNotFoundException("Instituição não encontrada");
        }

        // Valida se usuário já existe
        Optional<User> userOptional = userRepository.findByEmail(newUser.getEmail());
        if(userOptional.isPresent()){
            throw new BusinessRuleException("E-mail já cadastrado");
        }

        // Cria usuário
        User user = new User();
        user = mapper.modelMapper().map(newUser, User.class);
        user.setInstituition(institutionOptional.get());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setCreateAt(now());
        user.setActive(true);
        userRepository.save(user);
    }
    

    public UserReturnDto getUser(String email) {
        // 🔑 Instituição vem do TOKEN, não do request
        UUID institutionId = SecurityUtils.getInstitutionId();

        Instituition institution = instituitionRepository.findByidInstituition(institutionId)
                .orElseThrow(() ->new ResourceNotFoundException("Instituição não encontrada"));

        // Valida se usuário não existe
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException("E-mail não cadastrado");
        }
        UserReturnDto user = mapper.modelMapper().map(userOptional.get(), UserReturnDto.class);
        return user;
    }
}
