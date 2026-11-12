package com.proofchain.user.applications.command;

import com.proofchain.user.interfaces.dto.request.RecoveryPasswordDto;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecoveryPasswordCommand {

    private String email;
    private String password;

    public RecoveryPasswordCommand(@Valid RecoveryPasswordDto commnand) {
        this.email = commnand.email();
        this.password = commnand.password();
    }
}
