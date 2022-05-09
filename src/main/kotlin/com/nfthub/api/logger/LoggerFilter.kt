package com.nfthub.api.logger

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nfthub.api.controller.ExceptionController
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/*"])
class LoggerFilter : Filter {
    companion object {
        val logger: Logger = LoggerFactory.getLogger("com.nfthub.api")
        const val userIdHeader = "X-FORWARDED-FOR"
        const val userAgentHeader = "user-agent"
        val mapper = jacksonObjectMapper()
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request !is HttpServletRequest || response !is HttpServletResponse) {
            chain.doFilter(request, response)
            logger.error("request or response is not HttpServletRequest")
            return
        }

        val resWrapper = ContentCachingResponseWrapper(response)
        chain.doFilter(request, resWrapper)

        logging(request, resWrapper)
        resWrapper.copyBodyToResponse()
    }

    private fun logging(req: HttpServletRequest, resWrapper: ContentCachingResponseWrapper) {
        if (resWrapper.status < 400) {
            logger.info(
                InfoLog(
                    resStatus = resWrapper.status,
                    method = req.method,
                    requestUrl = req.requestURL.toString(),
                    userIp = req.getHeader(userIdHeader) ?: req.remoteAddr,
                    userAgent = req.getHeader(userAgentHeader)
                ).serialize()
            )
            return
        }


        if (resWrapper.status < 500) {
            val errorResponse = resWrapper.getErrorResponse()
            logger.warn(
                ErrorLog(
                    resStatus = resWrapper.status,
                    method = req.method,
                    requestUrl = req.requestURI.toString(),
                    userIp = req.getHeader(userIdHeader) ?: req.remoteAddr,
                    userAgent = req.getHeader(userAgentHeader),
                    message = errorResponse.message,
                    exception = errorResponse.exception
                ).serialize()
            )
            return
        }

        if (resWrapper.status > 500) {
            val errorResponse = resWrapper.getErrorResponse()
            logger.error(
                ErrorLog(
                    resStatus = resWrapper.status,
                    method = req.method,
                    requestUrl = req.requestURI.toString(),
                    userIp = req.getHeader(userIdHeader) ?: req.remoteAddr,
                    userAgent = req.getHeader(userAgentHeader),
                    message = errorResponse.message,
                    exception = errorResponse.exception
                ).serialize()
            )
            return
        }

        if (resWrapper.status == 500)
            resWrapper.loggingInternalServerError(req)
    }

    private fun ContentCachingResponseWrapper.loggingInternalServerError(req: HttpServletRequest) =
        let {
            val errorResponseString = String(it.contentAsByteArray, charset(it.characterEncoding))
            try {
                // 파싱이 제대로 된다면 ExceptionController 처리
                mapper.readValue(errorResponseString, ExceptionController.ErrorResponse::class.java)
            } catch (e: Exception) {
                logger.error(
                    InternalServerErrorLog(
                        resStatus = it.status,
                        method = req.method,
                        requestUrl = req.requestURI.toString(),
                        userIp = req.getHeader(userIdHeader) ?: req.remoteAddr,
                        userAgent = req.getHeader(userAgentHeader),
                        message = e.message,
                        exception = e.javaClass.name,
                        errorStack = e.stackTraceToString().slice(0..500)
                    ).serialize()
                )
            }
        }

    private fun ContentCachingResponseWrapper.getErrorResponse(): ExceptionController.ErrorResponse = let {
        val errorResponseString = String(it.contentAsByteArray, charset(it.characterEncoding))
        return try {
            mapper.readValue(errorResponseString, ExceptionController.ErrorResponse::class.java)
        } catch (e: Exception) {
            ExceptionController.ErrorResponse(message = e.localizedMessage, exception = e.javaClass.toString())
        }
    }


}
