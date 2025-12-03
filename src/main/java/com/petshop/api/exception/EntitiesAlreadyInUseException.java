package com.petshop.api.exception;

public class EntitiesAlreadyInUseException extends RuntimeException {
    public EntitiesAlreadyInUseException(String message) {
        super(message);
    }
}
