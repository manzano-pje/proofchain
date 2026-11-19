package com.proofchain.shared.exception;

public class PasswordExceptions extends BaseException {
    public PasswordExceptions(String message){
        super(message,409);
    }
}
