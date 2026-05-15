package com.proofchain.user.interfaces.controller;

import com.proofchain.user.applications.command.CreateUserCommand;
import com.proofchain.user.applications.command.UpdateUserCommand;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDto user) {
        CreateUserCommand command = new CreateUserCommand(user);
        createUser.createUser(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<UserReturn> listOneUser(@PathVariable String email) {
        UserReturn user = listOneUser.listOneUser(email);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public List<UserReturn> listAllUser(){
        return listAllUser.listAllUser();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<UserReturn> updateUser(@PathVariable Long id,
                                                 @RequestBody UserUpdateDto userUpdateDto){
        UpdateUserCommand command = new UpdateUserCommand(userUpdateDto);
        UserReturn user = updateUser.updateUser(id, command);
        return ResponseEntity.ok().body(user);
    }

    @PreAuthorize("hasHole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        deleteUser.deleteUSer(id);
        return ResponseEntity.ok().body("Usuário apagado com sucesso.");
    }
}
