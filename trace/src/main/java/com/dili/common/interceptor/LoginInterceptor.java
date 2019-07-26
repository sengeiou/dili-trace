package com.dili.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.BaseResult;
import com.dili.common.entity.SessionContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.lang.annotation.Annotation;

/**
 * 
 * @author xuliang
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	   
	    @Resource
	    private SessionContext sessionContext;
	    @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	        if(!(handler instanceof HandlerMethod)){
	        	return true;
			}
			InterceptConfiguration interceptConfiguration = findAnnotation((HandlerMethod) handler, InterceptConfiguration.class);
			if(interceptConfiguration==null || !interceptConfiguration.loginRequired()){
				return true;
			}
	        if(sessionContext.getAccountId()==null){//用户id为空表示未登陆
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				BaseResult baseResult=BaseResult.noLogin();
                Writer writer=response.getWriter();
                writer.write(JSONObject.toJSONString(baseResult));
                writer.flush();
                if(writer!=null){
                    writer.close();
                }
                return false;
			}
			return true;
	    }

	    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
	        T annotation = handler.getMethodAnnotation(annotationType);
	        if (annotation != null) return annotation;
	        return handler.getBeanType().getAnnotation(annotationType);
	    }
}
