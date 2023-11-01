package com.example.RestApi.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String customerReference) {
        super(String.format("Customer with reference %s not found!", customerReference));
    }
}
