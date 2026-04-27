package com.proofchain.course.domain.exception;

public class CourseIsRegisteredException extends RuntimeException {
    public CourseIsRegisteredException(){

        super("Instituicao não encontrada");
    }
}
