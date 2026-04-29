package com.proofchain.user;

import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.instituition.Instituition;
import com.proofchain.plataform.domain.ModelMapperConfig;
import com.proofchain.security.SecurityUtils;
import com.proofchain.user.dto.request.UserRequestDto;
import com.proofchain.user.dto.request.UserUpdateDto;
import com.proofchain.user.dto.response.UserReturn;
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

    public UserReturn createUser(UserRequestDto newUser) {
        // 🔑 Instituição vem do TOKEN, não do request
        Long instituitionId = SecurityUtils.getInstituitionId();
        Instituition instituition = validations.validateinstitution(instituitionId);

        // Valida se usuário já existe
        validations.validateUserExist(newUser.getEmail(), instituitionId);

        // Cria usuário
        User user = new User();
        user = mapper.modelMapper().map(newUser, User.class);
        user.setInstituition(instituition);
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setCreateAt(now());
        user.setActive(true);
        user = userRepository.save(user);

        return mapper.modelMapper().map(user, UserReturn.class);
    }

    public UserReturn getUser(String email) {
        // 🔑 Instituição vem do TOKEN, não do request
        Long instituitionId = SecurityUtils.getInstituitionId();
        Instituition instituition = validations.validateinstitution(instituitionId);;

        // Valida se usuário não existe
        Optional<User> userOptional = validations.validateUserNotExist(email, instituitionId);

        UserReturn user = mapper.modelMapper().map(userOptional.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado.")), UserReturn.class);
        return user;
    }

    public List<UserReturn> getAllUser(){
        // 🔑 Instituição vem do TOKEN, não do request
        Long instituitionId = SecurityUtils.getInstituitionId();
        Instituition instituition = validations.validateinstitution(instituitionId);

        List<User> userList = userRepository.findAll();
        if(userList.isEmpty()){
            throw new ResourceNotFoundException("Não há usuários cadsatrados.");
        }

        return userList.stream()
                .map(UserReturn::new)
                .collect(Collectors.toList());
    }

    public UserReturn updateUser(String email, UserUpdateDto userUpadte){
        // 🔑 Instituição vem do TOKEN, não do request
        Long instituitionId = SecurityUtils.getInstituitionId();
        Instituition instituition = validations.validateinstitution(instituitionId);

        Optional<User> userOptional = validations.validateUserNotExist(email, instituitionId);

        User user = new User();
        user.setId(userOptional.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado.")).getId());
        user.setName(userUpadte.getName());
        user.setRole(userUpadte.getRole());
        user.setActive(userUpadte.isActive());
        user.setUpdateAt(now());         
        user = userRepository.save(user);

        return mapper.modelMapper().map(user, UserReturn.class);
    }

    public void deleteUSer(String email){
        // 🔑 Instituição vem do TOKEN, não do request
        Long instituitionId = SecurityUtils.getInstituitionId();
        Instituition instituition = validations.validateinstitution(instituitionId);

        Optional<User> userOptional = validations.validateUserNotExist(email, instituitionId);
        userRepository.deleteByEmail(email);
    }
}
