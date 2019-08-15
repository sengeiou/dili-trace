package com.dili.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.ss.domain.BaseOutput;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.lang.annotation.Annotation;

/**
 * 
 * @author xuliang
 *
 */
public class SignInterceptor extends HandlerInterceptorAdapter {

	    @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	        if(!(handler instanceof HandlerMethod)){
	        	return true;
			}
			InterceptConfiguration interceptConfiguration = findAnnotation((HandlerMethod) handler, InterceptConfiguration.class);
			if(interceptConfiguration!=null && interceptConfiguration.signRequired()){
				String sign = request.getHeader("sign");
				if(!interceptConfiguration.signValue().equals(sign)){
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					BaseOutput baseOutput = BaseOutput.failure("签名不对");
					Writer writer=response.getWriter();
					writer.write(JSONObject.toJSONString(baseOutput));
					writer.flush();
					if(writer!=null){
						writer.close();
					}
					return false;
				}
			}

			return true;
	    }

	    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
	        T annotation = handler.getMethodAnnotation(annotationType);
	        if (annotation != null) return annotation;
	        return handler.getBeanType().getAnnotation(annotationType);
	    }
}
