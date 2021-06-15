package com.dili.trace.crack;

import com.dili.ss.retrofitful.aop.annotation.Order;
import com.dili.ss.retrofitful.aop.filter.Filter;
import com.dili.ss.retrofitful.aop.invocation.Invocation;
import com.dili.ss.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestfulInterfaceHandler<T> implements InvocationHandler, Serializable {

    protected static final Logger logger = LoggerFactory.getLogger(RestfulInterfaceHandler.class);

    private static final long serialVersionUID = -109085454821564L;
    // 代理的类名
    private Class<T> proxyClazz;
    //是否初始化
    private boolean inited = false;
    //restfulFilter缓存
    private Map<String, Filter> restfulFilterMap;
    private List<Filter> restfulFilters;

    /**
     * 约定的构造器
     *
     * @param proxyClazz
     */
    public RestfulInterfaceHandler(Class<T> proxyClazz) {
        this.proxyClazz = proxyClazz;
    }

    /**
     * 初始化RestfulAspect接口的所有bean实例
     */
    @SuppressWarnings("unchecked")
    private synchronized void init(){
        if(this.inited){
            return;
        }
        this.restfulFilterMap = SpringUtil.getBeansOfType(Filter.class);
        if(this.restfulFilterMap.isEmpty()){
            this.inited = true;
            return;
        }
        this.restfulFilters = new ArrayList<>(this.restfulFilterMap.values());
        if(this.restfulFilters.size() > 1) {
            //按@Order从小到大排序
            this.restfulFilters.sort((t1, t2) -> {
                Order order1 = t1.getClass().getAnnotation(Order.class);
                if (order1 == null) {
                    return 1;
                }
                Order order2 = t2.getClass().getAnnotation(Order.class);
                if (order2 == null) {
                    return 1;
                }
                return order1.value() >= order2.value() ? 1 : -1;
            });
            //有多个Filters时构建Filter链，就像链表，将指针指向下一个Filter
            for (int i = 1; i < this.restfulFilters.size(); i++) {
                final Filter restfulFilter = this.restfulFilters.get(i);
                this.restfulFilters.get(i - 1).setRestfulFilter(restfulFilter);
            }
        }
        this.inited = true;
    }

    /**
     * 当前的代理接口
     *
     * @return proxyClazz
     */
    @SuppressWarnings("unchecked")
    public Class<T> getProxyClazz() {
        return this.proxyClazz;
    }

    /**
     * 设置委托的接口
     * @param proxyClazz
     */
    @SuppressWarnings("unchecked")
    void setProxyClazz(Class<T> proxyClazz) {
        this.proxyClazz = proxyClazz;
    }

    /**
     * 进行代理调用
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(!this.inited){
            this.init();
        }
        if(this.restfulFilters == null){
            return null;
        }
        Invocation invocation = new Invocation();
        invocation.setArgs(args);
        invocation.setMethod(method);
        invocation.setProxy(proxy);
        invocation.setProxyClazz(this.proxyClazz);
        return this.restfulFilters.get(0).invoke(invocation);
    }

}