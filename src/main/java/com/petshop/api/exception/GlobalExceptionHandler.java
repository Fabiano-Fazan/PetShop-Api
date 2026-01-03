package com.petshop.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity<StandardError> cpfAlreadyExists(CpfAlreadyExistsException e, HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.CONFLICT, "Conflict", request.getRequestURI());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<StandardError> insufficientStock(InsufficientStockException e, HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, "Bad Request", request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, "Not Found", request.getRequestURI());
    }

    @ExceptionHandler({AppointmentDateTimeAlreadyExistsException.class, BusinessException.class})
    public ResponseEntity<StandardError> conflictExceptions(RuntimeException e, HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.CONFLICT, "Conflict", request.getRequestURI());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardError> runtimeException(HttpServletRequest request) {
        String customMessage = "Internal error, please contact the administrator.";
        return buildErrorResponse(customMessage, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationError(HttpServletRequest request) {
        String customMessage = "Email or password is invalid";
        return buildErrorResponse(customMessage, HttpStatus.UNPROCESSABLE_ENTITY, "Validation Error", request.getRequestURI());
    }

    private ResponseEntity<StandardError> buildErrorResponse(Exception e, HttpStatus status, String error, String path) {
        return buildErrorResponse(e.getMessage(), status, error, path);
    }

    private ResponseEntity<StandardError> buildErrorResponse(String message, HttpStatus status, String error, String path) {
        StandardError err = new StandardError();
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(status.value());
        err.setError(error);
        err.setMessage(message);
        err.setPath(path);
        return ResponseEntity.status(status).body(err);
    }

}
