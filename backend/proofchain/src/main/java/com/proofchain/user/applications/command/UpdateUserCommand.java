package com.proofchain.user.applications.command;

import com.proofchain.identities.enums.UserRole;
import com.proofchain.user.interfaces.dto.request.UserUpdateDto;
import lombok.Getter;

@Getter
public class UpdateUserCommand {

    private String name;
    private String email;
    private UserRole role;
    private boolean isActive;

    public UpdateUserCommand(UserUpdateDto dto){
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.role = dto.getRole();
        this.isActive = dto.isActive();
    }
}
