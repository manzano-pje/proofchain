package com.proofchain.controller;

import com.proofchain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

//    private final UserService userService;

//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody userRegisterDto user) {
//        return userService.registerUser(user);
//    }
}
