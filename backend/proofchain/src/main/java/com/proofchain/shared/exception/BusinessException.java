package com.proofchain.shared.exception;

public class BusinessException extends BaseException {
    public BusinessException(String message){
        super(message, 422);
    }
}
