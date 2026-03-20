package com.proofchain.service;

import com.proofchain.Dtos.request.UserRequestDto;
import com.proofchain.Dtos.response.UserReturnDto;
import com.proofchain.Dtos.request.UserUpdateDto;
import com.proofchain.configuration.ModelMapperConfig;
import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.Instituition;
import com.proofchain.identities.User;
import com.proofchain.repository.InstituitionRepository;
import com.proofchain.repository.UserRepository;
import com.proofchain.security.SecurityUtils;
import com.proofchain.util.Validations;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapperConfig mapper;
    private final Validations validations;

    public UserReturnDto createUser(UserRequestDto newUser) {
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Instituition institution = validations.validateInstituition(institutionId);

        // Valida se usuário já existe
        validations.validateUserExist(newUser.getEmail(), institutionId);

        // Cria usuário
        User user = new User();
        user = mapper.modelMapper().map(newUser, User.class);
        user.setInstituition(institution);
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setCreateAt(now());
        user.setActive(true);
        user = userRepository.save(user);

        return mapper.modelMapper().map(user, UserReturnDto.class);
    }

    public UserReturnDto getUser(String email) {
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Instituition institution = validations.validateInstituition(institutionId);

        // Valida se usuário não existe
        Optional<User> userOptional = validations.validateUserNotExist(email, institutionId);

        UserReturnDto user = mapper.modelMapper().map(userOptional.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado.")), UserReturnDto.class);
        return user;
    }

    public List<UserReturnDto> getAllUser(){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Instituition institution = validations.validateInstituition(institutionId);

        List<User> userList = userRepository.findAll();
        if(userList.isEmpty()){
            throw new ResourceNotFoundException("Não há usuários cadsatrados.");
        }

        return userList.stream()
                .map(UserReturnDto::new)
                .collect(Collectors.toList());
    }

    public UserReturnDto updateUser(String email, UserUpdateDto userUpadte){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Instituition institution = validations.validateInstituition(institutionId);

        Optional<User> userOptional = validations.validateUserNotExist(email, institutionId);

        User user = new User();
        user.setId(userOptional.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado.")).getId());
        user.setName(userUpadte.getName());
        user.setRole(userUpadte.getRole());
        user.setActive(userUpadte.isActive());
        user.setUpdateAt(now());         
        user = userRepository.save(user);

        return mapper.modelMapper().map(user, UserReturnDto.class);
    }

    public void deleteUSer(String email){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();
        Instituition institution = validations.validateInstituition(institutionId);

        Optional<User> userOptional = validations.validateUserNotExist(email, institutionId);
        userRepository.deleteByEmail(email);
    }
}
