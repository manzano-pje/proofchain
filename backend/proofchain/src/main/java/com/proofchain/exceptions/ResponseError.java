package com.proofchain.exceptions;

public record ResponseError (
        String message,
        Integer code
) {}
