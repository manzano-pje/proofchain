package com.proofchain.admin.featurePlan.domain.exception;

public class FeatureIsRegisteredException extends RuntimeException{
    public FeatureIsRegisteredException(){
        super("Característica do plano já cadatrada");
    }
}
