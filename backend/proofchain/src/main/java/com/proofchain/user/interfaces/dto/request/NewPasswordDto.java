package com.proofchain.user.interfaces.dto.request;

public record NewPasswordDto(
        String email,
        String Password
) {
}
