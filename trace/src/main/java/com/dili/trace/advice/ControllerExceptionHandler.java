package com.dili.trace.advice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 打印异常信息
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 处理exception
     *
     * @param request
     * @param ex
     * @return
     * @throws Throwable
     */
    @ExceptionHandler()
    public Object handleServiceMessageException(HttpServletRequest request, Throwable ex) throws Throwable {
        logger.error(ex.getMessage(), ex);
        throw ex;
    }
}