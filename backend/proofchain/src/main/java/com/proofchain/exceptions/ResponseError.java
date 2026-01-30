package com.proofchain.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ResponseError extends RuntimeException{

    private String mensage;
    private Integer code;

    public ResponseError(String mensage, Integer code){
        this.mensage = mensage;
        this.code = code;
    }

}