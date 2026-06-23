package com.proofchain.shared.exception;

public record ResponseError(
        String message,
        Integer statusCode)
 {}

