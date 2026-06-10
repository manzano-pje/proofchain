package com.proofchain.institution.domain.exception;

public class InstitutionNotFoundException extends RuntimeException {
    public InstitutionNotFoundException(){

        super("Instituição não encontrada.");
    }
}
