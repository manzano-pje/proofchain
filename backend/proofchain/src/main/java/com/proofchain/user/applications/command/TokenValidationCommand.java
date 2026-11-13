package com.proofchain.user.applications.command;

import com.proofchain.user.interfaces.dto.request.TokenValidationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenValidationCommand{
        private String email;
        private String token;

        public TokenValidationCommand(TokenValidationDto command) {
                this.email = command.email();
                this.token = command.token();
        }
}
