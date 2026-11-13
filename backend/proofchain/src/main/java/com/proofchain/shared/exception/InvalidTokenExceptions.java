package com.proofchain.shared.exception;

public class InvalidTokenExceptions extends BaseException {
    public InvalidTokenExceptions(String message){
        super(message,401);
    }
}
