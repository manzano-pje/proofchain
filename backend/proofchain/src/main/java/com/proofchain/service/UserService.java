package com.proofchain.service;

import com.proofchain.Dtos.UserRequestDto;
import com.proofchain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<String> registerUser(UserRequestDto user) {

        return null;
    }
}
