package com.proofchain.shared.exception;

public class AlreadyExistsException extends BaseException {
    public AlreadyExistsException(String message){
        super(message,409);
    }
}
