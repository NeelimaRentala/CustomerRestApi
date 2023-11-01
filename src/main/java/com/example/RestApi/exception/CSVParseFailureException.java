package com.example.RestApi.exception;

public class CSVParseFailureException extends RuntimeException {
    public CSVParseFailureException(String message) {
        super(message);
    }
}
