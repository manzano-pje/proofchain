package com.proofchain.shared.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseError> handleBaseException(BaseException ex){
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ResponseError(
                        ex.getMessage(),
                        ex.getStatusCode()));
    }
//
////     500 - Erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleGenericException(Exception ex){
        return ResponseEntity
                .status(500)
                .body(new ResponseError("Erro interno do servidor.",500
                ));
    }
}
