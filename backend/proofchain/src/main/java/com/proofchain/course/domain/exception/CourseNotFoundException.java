package com.proofchain.course.domain.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(){

        super("Curso não encontrado");
    }
}
