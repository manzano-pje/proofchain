package com.proofchain.admin.institution.domain.exception;

import com.proofchain.shared.exception.BaseException;

public class InstitutionAlerdyExistException extends BaseException {
    public InstitutionAlerdyExistException() {

        super("Instituição já cadastrada.", 409);
    }
}
