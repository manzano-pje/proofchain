package com.proofchain.admin.plan.domain.exception;

public class PlanNotFoundException extends RuntimeException{
    public PlanNotFoundException(){

        super("Plano não cadastrado");
    }
}
