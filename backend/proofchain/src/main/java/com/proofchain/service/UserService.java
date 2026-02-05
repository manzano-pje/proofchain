package com.proofchain.service;

import com.proofchain.Dtos.UserRequestDto;
import com.proofchain.Dtos.UserReturnDto;
import com.proofchain.Dtos.UserUpdateDto;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        UUID institutionId = SecurityUtils.getInstitutionId();

        Instituition institution = instituitionRepository.findByidInstituition(institutionId)
            .orElseThrow(() ->new ResourceNotFoundException("Instituição não encontrada"));

        // Valida se usuário já existe
        Optional<User> userOptional = userRepository.findByEmail(newUser.getEmail());
        if(userOptional.isPresent()){
            throw new BusinessRuleException("Usuário já cadastrado");
        }

        // Cria usuário
        User user = new User();
        user = mapper.modelMapper().map(newUser, User.class);
        user.setInstituition(institution);
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
            throw new ResourceNotFoundException("Usuário não cadastrado");
        }
        UserReturnDto user = mapper.modelMapper().map(userOptional.get(), UserReturnDto.class);
        return user;
    }

    public List<UserReturnDto> getAllUser(){
        // 🔑 Instituição vem do TOKEN, não do request
        UUID institutionId = SecurityUtils.getInstitutionId();

        Instituition institution = instituitionRepository.findByidInstituition(institutionId)
                .orElseThrow(() ->new ResourceNotFoundException("Instituição não encontrada"));

        List<User> userList = userRepository.findAll();
        if(userList.isEmpty()){
            throw new ResourceNotFoundException("Não há usuários cadsatrados.");
        }

        return userList.stream()
                .map(UserReturnDto::new)
                .collect(Collectors.toList());
    }

    public void updateUser(String email, UserUpdateDto userUpadte){
        // 🔑 Instituição vem do TOKEN, não do request
        UUID institutionId = SecurityUtils.getInstitutionId();

        Instituition institution = instituitionRepository.findByidInstituition(institutionId)
                .orElseThrow(() ->new ResourceNotFoundException("Instituição não encontrada"));

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException("Usuário não cadastrado");
        }

        User user = new User();
        user.setId(userOptional.get().getId());
        user.setName(userUpadte.getName());
        user.setRole(userUpadte.getRole());
        user.setActive(userUpadte.isActive());
        user.setUpdateAt(now());         
        userRepository.save(user);
    }

    public void deleteUSer(String email){
        // 🔑 Instituição vem do TOKEN, não do request
        UUID institutionId = SecurityUtils.getInstitutionId();

        Instituition institution = instituitionRepository.findByidInstituition(institutionId)
                .orElseThrow(() ->new ResourceNotFoundException("Instituição não encontrada"));

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException("Usuário não cadastrado");
        }
        userRepository.deleteByEmail(email);
    }
}
