package com.proofchain.course.domain.exception;

public class InstrituitionNotFoundException extends RuntimeException {
    public InstrituitionNotFoundException(){

        super("Instituicao não encontrada");
    }
}
