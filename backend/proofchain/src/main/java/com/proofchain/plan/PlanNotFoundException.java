package com.proofchain.plan;

public class PlanNotFoundException extends RuntimeException{
    public PlanNotFoundException(){
        super("Plano não cadastrado");
    }
}
