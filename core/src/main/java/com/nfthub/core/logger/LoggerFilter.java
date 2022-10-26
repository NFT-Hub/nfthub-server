package com.nfthub.core.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nfthub.core.exception.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class LoggerFilter implements Filter {
    public static final String USER_IP_HEADER = "X-FORWARDED-FOR";
    public static final String USER_AGENT_HEADER = "user-agent";
    public static final Logger logger = LoggerFactory.getLogger(LoggerFilter.class.getName());
    static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getUserIp(HttpServletRequest request) {
        if (request.getHeader(USER_IP_HEADER) == null) {
            return request.getHeader(USER_IP_HEADER);
        }
        return request.getRemoteAddr();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletRequest)) {
            chain.doFilter(servletRequest, servletResponse);
            logger.error("request or response is not HttpServletRequest");
            return;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);
        chain.doFilter(request, cachingResponseWrapper);
        logging(request, cachingResponseWrapper);
    }

    private void logging(HttpServletRequest request, ContentCachingResponseWrapper response) {
        if (response.getStatus() < 400) {
            infoLogging(request, response);
            return;
        }

        if (response.getStatus() != 500) {
            exceptionLogging(request, response);
        }

        // 500번은 ExceptionController 로깅
    }

    private void infoLogging(HttpServletRequest request, HttpServletResponse response) {
        InfoLog infoLog = new InfoLog();
        infoLog.setMethod(request.getMethod());
        infoLog.setResStatus(response.getStatus());
        infoLog.setRequestUrl(request.getRequestURI());
        infoLog.setUserIp(getUserIp(request));
        infoLog.setUserAgent(request.getHeader(USER_AGENT_HEADER));
        logger.info(infoLog.serialize());
    }

    private void exceptionLogging(HttpServletRequest request, ContentCachingResponseWrapper response) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setMethod(request.getMethod());
        errorLog.setResStatus(response.getStatus());
        errorLog.setRequestUrl(request.getRequestURI());
        errorLog.setUserIp(getUserIp(request));
        errorLog.setUserAgent(request.getHeader(USER_AGENT_HEADER));
        errorLog.setException(getExceptionResponse(response).getException());
        logger.error(errorLog.serialize());
    }

    private ExceptionResponse getExceptionResponse(ContentCachingResponseWrapper response) {
        ExceptionResponse exceptionResponse;
        try {
            String exceptionResponseString = new String(response.getContentAsByteArray(), response.getCharacterEncoding());
            exceptionResponse = objectMapper.readValue(exceptionResponseString, ExceptionResponse.class);
        } catch (Exception e) {
            exceptionResponse = new ExceptionResponse();
            exceptionResponse.setMessage(e.getLocalizedMessage());
            exceptionResponse.setException(e.getClass().getName());
        }
        return exceptionResponse;
    }
}
