package com.proofchain.shared.exception;

public class NotAuthorizedException extends BaseException{
    public NotAuthorizedException(){
        super("Acesso não autorizado", 403);
    }
}
