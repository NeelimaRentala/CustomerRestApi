package com.example.RestApi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomerControllerAdvice {

    @ResponseBody
    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String customerNotFound(CustomerNotFoundException customerNotFoundException){
        return customerNotFoundException.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CSVParseFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String customerNotFound(CSVParseFailureException cSVParseFailureException){
        return cSVParseFailureException.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CSVInvalidDirectoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String customerNotFound(CSVInvalidDirectoryException csvInvalidDirectoryException){
        return csvInvalidDirectoryException.getMessage();
    }
}
