package com.proofchain.course.domain.exception;

import com.proofchain.shared.exception.BaseException;

public class CourseIsRegisteredException extends BaseException {
    public CourseIsRegisteredException(){

        super("Curso já cadastrado", 409);
    }
}
