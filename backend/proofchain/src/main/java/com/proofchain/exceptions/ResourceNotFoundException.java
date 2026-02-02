package com.proofchain.exceptions;

public class ResourceNotFoundException extends ResponseError {
    public ResourceNotFoundException(String message){
        super(message,404);
    }
}
