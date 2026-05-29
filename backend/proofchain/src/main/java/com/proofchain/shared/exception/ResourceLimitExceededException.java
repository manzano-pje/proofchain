package com.proofchain.shared.exception;

public class ResourceLimitExceededException extends BusinessException{
    public ResourceLimitExceededException(String message){
        super(message);
    }
//    Limite de cursos atingido
//    Limite de alunos atingido
//    Limite de certificados atingido
//    Limite de usuários atingido
}
