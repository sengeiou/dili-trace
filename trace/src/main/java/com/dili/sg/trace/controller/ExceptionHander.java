package com.dili.sg.trace.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // "控制器增强"注解
public class ExceptionHander {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHander.class);

	@ExceptionHandler(Throwable.class) // 用于注释异常处理类，value属性指定需要拦截的异常类型
	public void errorHandler(Throwable e) throws Throwable { // 捕获异常并获取异常处理对象
		logger.error(e.getMessage(), e);
		throw e;
	}
}