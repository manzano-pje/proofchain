package com.proofchain.user.interfaces.dto.request;

public record RecoveryPasswordDto(
        String email,
        String password
) {
}
