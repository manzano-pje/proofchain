package com.proofchain.admin.featurePlan.domain.exception;

public class FeatureAlerdyExistException extends RuntimeException{
    public FeatureAlerdyExistException(){
        super("Característica do plano já cadatrada");
    }
}
