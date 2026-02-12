package com.proofchain.exceptions;

public class BusinessRuleException  extends RuntimeException {
    public BusinessRuleException(String message){

        super(message);
    }
}
