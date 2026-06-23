package com.proofchain.course.domain.exception;

import com.proofchain.shared.exception.BaseException;

public class CourseNotFoundException extends BaseException {
    public CourseNotFoundException(){
        super("Curso não encontrado",404);
    }
}
