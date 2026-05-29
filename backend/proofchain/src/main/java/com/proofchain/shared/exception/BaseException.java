package com.proofchain.shared.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException{

    private final Integer statusCode;

    protected BaseException(String message, Integer statusCode){
        super(message);
        this.statusCode = statusCode;
    }
}
