package com.proofchain.user.interfaces.dto.request;

public record TokenValidationDto(
        String email,
        String token
) {}
