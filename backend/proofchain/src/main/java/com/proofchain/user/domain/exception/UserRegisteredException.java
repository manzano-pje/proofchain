package com.proofchain.user.domain.exception;

import com.proofchain.shared.exception.BaseException;

public class UserRegisteredException extends BaseException {
    public UserRegisteredException(){

        super("Usuário já cadastrado",409);
    }
}
