package com.proofchain.admin.institution.domain.exception;

import com.proofchain.shared.exception.BaseException;

public class InstitutionNotAutorizedException extends BaseException {
    public InstitutionNotAutorizedException(){

        super("Instituição não autorizado.",403);
    }
}
