package com.proofchain.Dtos.response;

public record ApiResponse(
        boolean success,
        String message,
        Object data
) {}
