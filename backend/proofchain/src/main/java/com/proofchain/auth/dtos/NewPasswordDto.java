package com.proofchain.auth.dtos;

public record NewPasswordDto(
        String email,
        String token,
        String password
) {
}
