package com.thirdeye3.messenger.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.exceptions.MessageBrokerException;
import com.thirdeye3.messenger.exceptions.PropertyFetchException;
import com.thirdeye3.messenger.exceptions.StockException;
import com.thirdeye3.messenger.exceptions.GroupException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PropertyFetchException.class)
    public ResponseEntity<Response<Void>> handlePropertyFetch(PropertyFetchException ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    @ExceptionHandler(StockException.class)
    public ResponseEntity<Response<Void>> handleStockException(StockException ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    @ExceptionHandler(MessageBrokerException.class)
    public ResponseEntity<Response<Void>> handleMessageBrokerException(MessageBrokerException ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    @ExceptionHandler(GroupException.class)
    public ResponseEntity<Response<Void>> handleUserException(GroupException ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGeneric(Exception ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error occurred: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

