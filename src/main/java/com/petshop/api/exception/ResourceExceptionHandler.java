package com.petshop.api.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity<StandardError> cpfAlreadyExists(CpfAlreadyExistsException e, HttpServletRequest request){

        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError();
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(status.value());
        err.setError("Conflict");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<StandardError> insufficientStock(InsufficientStockException e, HttpServletRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError();
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(status.value());
        err.setError("Bad Request");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError();
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(status.value());
        err.setError("Not Found");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AppointmentDateTimeAlreadyExistsException.class)
    public ResponseEntity<StandardError> appointmentDateTimeAlreadyExists(AppointmentDateTimeAlreadyExistsException e, HttpServletRequest request){

        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError();
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(status.value());
        err.setError("Conflict");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> businessException(BusinessException e, HttpServletRequest request){

        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError();
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(status.value());
        err.setError("Conflict");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
