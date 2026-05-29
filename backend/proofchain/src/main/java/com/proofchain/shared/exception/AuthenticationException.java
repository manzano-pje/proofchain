package com.proofchain.shared.exception;

public class AuthenticationException extends BaseException{
    public AuthenticationException(String message){
        super(message, 401);
    }
}
