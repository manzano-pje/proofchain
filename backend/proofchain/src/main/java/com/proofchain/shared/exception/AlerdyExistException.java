package com.proofchain.shared.exception;

public class AlerdyExistException extends BaseException{
    public AlerdyExistException(String message){
        super(message, 409);
    }
}
