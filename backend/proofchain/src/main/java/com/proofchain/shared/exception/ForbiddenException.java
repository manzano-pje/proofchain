package com.proofchain.shared.exception;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(message, 403);
    }
}
