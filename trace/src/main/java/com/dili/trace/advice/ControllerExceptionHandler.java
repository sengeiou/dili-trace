package com.dili.trace.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
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
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     * @throws Throwable
     */
    @ExceptionHandler(Exception.class)
    public Object handleServiceMessageException(
            HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) throws Throwable {
        if (handler != null) {
            logger.info("handler.class={}", handler.getClass());
        }
        logger.error(ex.getMessage(), ex);
        throw ex;
    }
}
