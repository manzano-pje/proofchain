package com.proofchain.user.interfaces.controller;

import com.proofchain.user.applications.command.CreateUserCommand;
import com.proofchain.user.applications.handler.*;
import com.proofchain.user.interfaces.dto.request.UserRequestDto;
import com.proofchain.user.interfaces.dto.request.UserUpdateDto;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final CreateUserHandler createUser;
    private final DeleteUserHandler deleteUser;
    private final ListAllUserHandler listAllUser;
    private final ListOneUserHandler listOneUser;
    private final UpdateUserHandler updateUser;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<UserReturn> createUser(@RequestBody UserRequestDto user) {
        CreateUserCommand command = new CreateUserCommand(user);
        UserReturn response = createUser.createUser(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<UserReturn> listOneUser(@PathVariable String email) {
        UserReturn user = listOneUser.listOneUser(email);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<UserReturn> listAllUser(){
        return listAllUser.listAllUser();
    }

    @PreAuthorize("hasRole('ROLE_USER','ROLE_ADMIN')")
    @PatchMapping("/update/{email}")
    public ResponseEntity<UserReturn> updateUser(@PathVariable String email,
                                                 @RequestBody UserUpdateDto userUpdateDto){
        UserReturn user = updateUser.updateUser(email, userUpdateDto);
        return ResponseEntity.ok().body(user);
    }

    @PreAuthorize("hasHole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email){
        deleteUser.deleteUSer(email);
        return ResponseEntity.ok().body("Usuário apagado com sucesso.");
    }
}
