package com.proofchain.shared.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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

    //  401 - Unauthorized
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(Exception ex){
        ex.printStackTrace();   // <- adicionar
        return ResponseEntity
                .status(401)
                .body(new ResponseError(
                        ex.getMessage(),
                        401));
    }

    //  403 - Forbidden
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseError> handleForbiddenException(Exception ex){
        ex.printStackTrace();   // <- adicionar
        return ResponseEntity
                .status(403)
                .body(new ResponseError(
                        ex.getMessage(),
                        403));
    }

    //  404 - Forbidden
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ResponseError> handleInternalAuthenticationServiceException(Exception ex){
        ex.printStackTrace();   // <- adicionar
        return ResponseEntity
                .status(403)
                .body(new ResponseError(
                        ex.getMessage(),
                        403));
    }

//  500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleGenericException(Exception ex){
        ex.printStackTrace();   // <- adicionar
        return ResponseEntity
                .status(500)
                .body(new ResponseError(
                        "Erro interno do servidor.",
                        500
                ));
    }
}
