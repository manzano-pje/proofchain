package com.proofchain.shared.exception;

public class ResponseError extends BaseException{
    public ResponseError(String message, Integer statusCode){
        super(message, statusCode);
    }
}
