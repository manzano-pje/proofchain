package com.proofchain.user.domain.exception;

public class UserRegisteredException extends RuntimeException {
    public UserRegisteredException(){

        super("Usuário já encontrado");
    }
}
