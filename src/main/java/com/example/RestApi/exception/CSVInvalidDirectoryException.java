package com.example.RestApi.exception;

public class CSVInvalidDirectoryException extends RuntimeException {
    public CSVInvalidDirectoryException(String message) {
        super(message);
    }
}
