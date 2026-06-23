package com.proofchain.user.applications.command;

import com.proofchain.user.domain.model.UserRole;
import com.proofchain.user.interfaces.dto.request.UserUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserCommand {

    private String name;
    private UserRole role;
    private boolean isActive;

    public UpdateUserCommand(UserUpdateDto dto){
        this.name = dto.getName();
        this.role = dto.getRole();
        this.isActive = dto.isActive();
    }
}
