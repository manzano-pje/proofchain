package com.proofchain.user.applications.command;

import com.proofchain.identities.enums.UserRole;
import com.proofchain.user.interfaces.dto.request.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserCommand {
    private String name;
    private String email;
    private String password;
    private UserRole role;

    public CreateUserCommand(UserRequestDto dto){
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.role = dto.getRole();
    }
}
