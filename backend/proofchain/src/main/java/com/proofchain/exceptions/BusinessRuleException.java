package com.proofchain.exceptions;

public class BusinessRuleException  extends ResponseError {
    public BusinessRuleException(String message){

        super(message,404);
    }
}
