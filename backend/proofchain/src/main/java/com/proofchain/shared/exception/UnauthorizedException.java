package com.proofchain.shared.exception;

public class UnauthorizedException extends BaseException{
    public UnauthorizedException(String message){

        super(message, 401);
    }
}
