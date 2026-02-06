package com.proofchain.controller;

import com.proofchain.Dtos.UserRequestDto;
import com.proofchain.Dtos.UserReturnDto;
import com.proofchain.Dtos.UserUpdateDto;
import com.proofchain.repository.UserRepository;
import com.proofchain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<UserReturnDto> getAllUser(){
        return userService.getAllUser();
    }

    @PreAuthorize("hasRole('ROLE_USER','ROLE_ADMIN')")
    @PatchMapping("/update/{email}")
    public ResponseEntity<Void> updateUser(@PathVariable String email, @RequestBody UserUpdateDto userUpdateDto){
        userService.updateUser(email, userUpdateDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasHole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Void> deleteUSer(@PathVariable String email){
        userService.deleteUSer(email);
        return ResponseEntity.ok().build();
    }

}
