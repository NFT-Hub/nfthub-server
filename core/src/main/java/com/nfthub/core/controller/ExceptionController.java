package com.nfthub.core.controller;

import com.nfthub.core.exception.*;
import com.nfthub.core.logger.InternalServerErrorLog;
import com.nfthub.core.logger.LoggerFilter;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestController
@ControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ExceptionResponse handleException(Throwable throwable, HttpServletRequest request) {
        exceptionLogging(throwable, request);
        return ExceptionResponse.create(throwable);
    }

    private void exceptionLogging(Throwable throwable, HttpServletRequest request) {
        InternalServerErrorLog log = new InternalServerErrorLog();
        log.setResStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.setMethod(request.getMethod());
        log.setRequestUrl(request.getRequestURI());
        log.setUserIp(LoggerFilter.getUserIp(request));
        log.setUserAgent(request.getHeader(LoggerFilter.USER_AGENT_HEADER));
        log.setException(throwable);
        LoggerFilter.logger.error(log.serialize());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            MaxUploadSizeExceededException.class,
            BadRequestException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ExceptionResponse handleBadRequestException(MaxUploadSizeExceededException throwable) {
        return ExceptionResponse.create(throwable);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {
            UnAuthenticatedException.class
    })
    public ExceptionResponse handleUnAuthenticatedException(UnAuthenticatedException throwable) {
        return ExceptionResponse.create(throwable);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {
            ForbiddenException.class,
            AccessDeniedException.class,
            BadCredentialsException.class
    })
    public ExceptionResponse handleForbiddenException(ForbiddenException throwable) {
        return ExceptionResponse.create(throwable);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {
            NotFoundException.class
    })
    public ExceptionResponse handleNotFoundException(NotFoundException throwable) {
        return ExceptionResponse.create(throwable);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(NotImplementedException.class)
    public ExceptionResponse handleNotImplementedException(NotImplementedException throwable) {
        return ExceptionResponse.create(throwable, "Not Implemented Method");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {
            DataIntegrityViolationException.class,
            AlreadyExistException.class,
            ConflictException.class
    })
    public ExceptionResponse handleConflictException(DataIntegrityViolationException throwable) {
        return ExceptionResponse.create(throwable);
    }
}
