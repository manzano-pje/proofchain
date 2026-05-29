package com.proofchain.shared.exception;

public class ValidationException extends BaseException {
    public ValidationException(String message){
        super(message, 400);
    }
}
