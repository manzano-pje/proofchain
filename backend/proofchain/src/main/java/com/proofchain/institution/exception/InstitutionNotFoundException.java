package com.proofchain.institution.exception;

public class InstitutionNotFoundException extends RuntimeException {
    public InstitutionNotFoundException(){

        super("Instituicao não encontrada");
    }
}
