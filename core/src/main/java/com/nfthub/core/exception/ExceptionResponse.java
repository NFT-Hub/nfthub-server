package com.nfthub.core.exception;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String message;
    private String exception;

    public static ExceptionResponse create(Throwable throwable) {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(throwable.getMessage());
        response.setException(throwable.getClass().getName());
        return response;
    }

    public static ExceptionResponse create(Throwable throwable, String message) {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(message);
        response.setException(throwable.getClass().getName());
        return response;
    }
}
