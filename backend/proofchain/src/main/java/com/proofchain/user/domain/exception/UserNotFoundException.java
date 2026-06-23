package com.proofchain.user.domain.exception;

import com.proofchain.shared.exception.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(){

        super("Usuário não encontrado",404);
    }
}
