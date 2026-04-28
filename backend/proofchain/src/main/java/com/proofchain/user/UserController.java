package com.proofchain.user;

import com.proofchain.user.dto.request.UserRequestDto;
import com.proofchain.user.dto.response.UserReturn;
import com.proofchain.user.dto.request.UserUpdateDto;
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

    private final UserService userService;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<UserRequestDto> createUser(@RequestBody UserRequestDto user) {
        UserReturn usder = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);

    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<UserReturn> getUser(@PathVariable String email) {
        UserReturn user = userService.getUser(email);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<UserReturn> getAllUser(){
        return userService.getAllUser();
    }

    @PreAuthorize("hasRole('ROLE_USER','ROLE_ADMIN')")
    @PatchMapping("/update/{email}")
    public ResponseEntity<UserReturn> updateUser(@PathVariable String email,
                                                 @RequestBody UserUpdateDto userUpdateDto){
        UserReturn user = userService.updateUser(email, userUpdateDto);
        return ResponseEntity.ok().body(user);
    }

    @PreAuthorize("hasHole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email){
        userService.deleteUSer(email);
        return ResponseEntity.ok().body("Usuário apagado com sucesso.");
    }

}
