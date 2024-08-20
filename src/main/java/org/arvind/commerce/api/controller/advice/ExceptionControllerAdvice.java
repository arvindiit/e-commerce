package org.arvind.commerce.api.controller.advice;

import org.arvind.commerce.api.exception.InvalidRequestException;
import org.arvind.commerce.api.exception.OutOfStockException;
import org.arvind.commerce.api.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = {InvalidRequestException.class})
    public ResponseEntity<String> invalidRequestException(InvalidRequestException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<String> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {OutOfStockException.class})
    public ResponseEntity<String> outOfStockException(OutOfStockException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
