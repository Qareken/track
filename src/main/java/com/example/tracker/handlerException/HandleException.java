package com.example.tracker.handlerException;


import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class HandleException {
    @ExceptionHandler(BindException.class)
    public ResponseStatusException handleValidationException(BindException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getFieldErrors().forEach(fieldError -> {
            errorMessage.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append("; ");
        });
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());
    }
}
