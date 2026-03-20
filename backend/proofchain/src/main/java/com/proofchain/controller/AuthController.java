package com.proofchain.controller;

import com.proofchain.Dtos.request.AuthRequestDto;
import com.proofchain.Dtos.response.AuthResponseDto;
import com.proofchain.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponseDto> loginValidate(@RequestBody AuthRequestDto authRequestDto){
        String token = authService.loginValidadation(authRequestDto);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
