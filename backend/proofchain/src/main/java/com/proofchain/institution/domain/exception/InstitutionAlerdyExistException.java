package com.proofchain.institution.domain.exception;

public class InstitutionAlerdyExistException extends RuntimeException {
    public InstitutionAlerdyExistException() {

        super("Instituição não autorizado");
    }
}
