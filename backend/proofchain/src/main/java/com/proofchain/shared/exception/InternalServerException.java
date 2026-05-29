package com.proofchain.shared.exception;

import com.fasterxml.jackson.databind.ser.Serializers;

public class InternalServerException extends BaseException {
    public InternalServerException(String message){
        super(message, 500);
    }
}
