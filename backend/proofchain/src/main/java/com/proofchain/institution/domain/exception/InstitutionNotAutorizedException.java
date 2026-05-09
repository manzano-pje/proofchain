package com.proofchain.institution.domain.exception;

public class InstitutionNotAutorizedException extends RuntimeException {
    public InstitutionNotAutorizedException(){

        super("INstituição não autorizado");
    }
}
