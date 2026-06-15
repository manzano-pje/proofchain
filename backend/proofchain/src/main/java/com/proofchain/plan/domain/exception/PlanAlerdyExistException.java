package com.proofchain.plan.domain.exception;

public class PlanAlerdyExistException extends RuntimeException{
    public PlanAlerdyExistException(){
        super("Plano já cadastrado");
    }
}