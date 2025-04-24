package com.library.exceptions;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {

    private final HttpStatus httpStatus;

    public AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
    //now i want my app to intercept the exception and then return the error message and the Http code
    //=> i would use an aspect, an advise
    //=> com.library.config -- RestExceptionHandler
}
