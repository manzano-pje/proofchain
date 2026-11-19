package com.proofchain.user.applications.command;

import com.proofchain.user.interfaces.dto.request.NewPasswordDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewPasswordCommand {

    private String email;
    private String password;

    public NewPasswordCommand(NewPasswordDto commnand) {
        this.email = commnand.email();
        this.password = commnand.password();
    }
}
