package com.proofchain.user.interfaces.dto.request;

public record ResetPasswordDto(
        String email,
        String password
) {
}
