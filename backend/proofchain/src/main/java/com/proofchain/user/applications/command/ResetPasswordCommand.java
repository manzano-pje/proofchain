package com.proofchain.user.applications.command;

import com.proofchain.user.interfaces.dto.request.ResetPasswordDto;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResetPasswordCommand {

    private String email;
    private String password;

    public ResetPasswordCommand(ResetPasswordDto commnand) {
        this.email = commnand.email();
        this.password = commnand.password();
    }
}
