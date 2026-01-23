package com.proofchain.service;

import com.proofchain.identiies.User;
import com.proofchain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<String> registerUser(userRegisterDto user) {
}
