package com.proofchain.controller;

import com.proofchain.Dtos.UserRequestDto;
import com.proofchain.Dtos.UserReturnDto;
import com.proofchain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDto user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<UserReturnDto> getUser(@PathVariable String email) {
        UserReturnDto user = userService.getUser(email);
        return ResponseEntity.ok(user);
    }
}
