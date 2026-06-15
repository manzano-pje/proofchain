package com.proofchain.plan.domain.exception;

public class PlanNotFoundException extends RuntimeException{
    public PlanNotFoundException(){

        super("Plano não cadastrado");
    }
}
