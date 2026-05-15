package com.proofchain.course.domain.exception;

public class CourseIsRegisteredException extends RuntimeException {
    public CourseIsRegisteredException(){

        super("Curso já cadastrado");
    }
}
