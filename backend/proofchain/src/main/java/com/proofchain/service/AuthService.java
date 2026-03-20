package com.proofchain.service;


import com.proofchain.Dtos.request.AuthRequestDto;
import com.proofchain.Dtos.response.AuthResponseDto;
import com.proofchain.exceptions.ValidationException;
import com.proofchain.identities.User;
import com.proofchain.repository.UserRepository;
import com.proofchain.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public String loginValidadation(AuthRequestDto authRequestDto){

        Optional<User> userOptional = userRepository.findByEmail(authRequestDto.getEmail());
        if(userOptional.isEmpty()){
            throw new ValidationException("Usuário ou senha inválidos!");
        }

        if(!passwordEncoder.matches(authRequestDto.getPassword(), userOptional.get().getPassword())){
            throw new ValidationException("Usuário ou senha inválidos!");
        }
        return jwtService.generateToken(userOptional.get());
    }
}
