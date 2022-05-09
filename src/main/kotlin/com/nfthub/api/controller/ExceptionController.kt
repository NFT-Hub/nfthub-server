package com.nfthub.api.controller


import com.nfthub.api.logger.InternalServerErrorLog
import com.nfthub.api.logger.LoggerFilter
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MaxUploadSizeExceededException
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@RestController
@ControllerAdvice
class ExceptionController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable::class)
    fun handleThrowable(e: Throwable, req: HttpServletRequest): ErrorResponse {
        LoggerFilter.logger.error(
            InternalServerErrorLog(
                resStatus = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                method = req.method,
                requestUrl = req.requestURI.toString(),
                userIp = req.getHeader(LoggerFilter.userIdHeader) ?: req.remoteAddr,
                userAgent = req.getHeader(LoggerFilter.userAgentHeader),
                message = e.message,
                exception = e.javaClass.name,
                errorStack = e.stackTraceToString().slice(0..500)
            ).serialize()
        )
        return ErrorResponse.create(e)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMethodArgumentNotValidException(e: MaxUploadSizeExceededException): ErrorResponse =
        ErrorResponse(e.javaClass.name, e.message ?: "Maximum upload size exceeded")

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ErrorResponse = ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ErrorResponse =
        ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthenticatedException::class)
    fun handleUnAuthorizedException(e: UnAuthenticatedException) = ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(e: DataIntegrityViolationException): ErrorResponse =
        ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplementedError(e: NotImplementedError): ErrorResponse =
        ErrorResponse(e.javaClass.name, "Not Allow Method")

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ErrorResponse = ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistException::class)
    fun handleAlreadyExistException(e: AlreadyExistException): ErrorResponse = ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(e: ConflictException): ErrorResponse = ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(e: ForbiddenException): ErrorResponse = ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException): ErrorResponse = ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ErrorResponse =
        ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ErrorResponse =
        ErrorResponse.create(e)

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BadCredentialsException::class)
    fun handleAccessBadCredentialsException(e: BadCredentialsException): ErrorResponse =
        ErrorResponse.create(e)

    data class ErrorResponse(
        val exception: String,
        val message: String?,
    ) {
        companion object {
            fun create(throwable: Throwable): ErrorResponse {
                return ErrorResponse(
                    exception = throwable.javaClass.name,
                    message = throwable.message
                )
            }
        }
    }
}