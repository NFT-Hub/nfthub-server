package com.nfthub.core.logger;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

@Data
@EqualsAndHashCode(callSuper = false)
public class InternalServerErrorLog extends AbstractLog {
    private String exception;
    private String message;
    private String stackTrace;

    public void setException(Throwable throwable) {
        this.exception = throwable.getClass().getName();
        this.message = throwable.getMessage();
        this.stackTrace = Arrays.toString(throwable.getStackTrace()).substring(0, 500);
    }
}
