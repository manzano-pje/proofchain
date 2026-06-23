package com.proofchain.institution.domain.exception;

import com.proofchain.shared.exception.BaseException;

public class InstitutionAlerdyExistException extends BaseException {
    public InstitutionAlerdyExistException() {

        super("Instituição já cadastrada.", 409);
    }
}
