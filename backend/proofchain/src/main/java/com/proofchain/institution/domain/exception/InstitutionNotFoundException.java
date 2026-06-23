package com.proofchain.institution.domain.exception;

import com.proofchain.shared.exception.BaseException;

public class InstitutionNotFoundException extends BaseException {
    public InstitutionNotFoundException(){

        super("Instituição não encontrada.",404);
    }
}
