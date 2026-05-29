package com.proofchain.shared.exception;

public class NotAuthorizedException extends BaseException{
    public NotAuthorizedException(String message){
        super(message, 403);
    }
}
