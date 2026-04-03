package com.proofchain.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Erros de validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseError(mensagem, 400));
    }


    // 403 - Usuário ou senha incorretos
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> BadCredentialsException(
            BadCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseError("Usuário ou senha inválidos", 401));
    }

    // 404 - Recurso não encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseError> handleNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseError(ex.getMessage(), 404));
    }

    // 409 - Conflito / regra de negócio
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ResponseError> handleBusinessRule(
            BusinessRuleException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ResponseError(ex.getMessage(), 409));
    }

//     500 - Erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) throws Exception {
        String path = request.getRequestURI();

        // 🚫 Não interceptar Swagger / OpenAPI
        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")) {
            throw ex;
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseError("Erro interno do servidor", 500));
    }

    
}
