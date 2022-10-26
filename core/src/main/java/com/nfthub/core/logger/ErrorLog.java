package com.nfthub.core.logger;

import com.nfthub.core.exception.ExceptionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorLog extends AbstractLog {
    private String exception;
    private String message;

    public void setExceptionFields(ExceptionResponse exception) {
        this.exception = exception.getException();
        this.message = exception.getMessage();
    }
}
