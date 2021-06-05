package com.dili.trace.api;

import com.dili.common.exception.TraceBizException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class AbstractApi {
    @Autowired
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    public String toJSONString(Object object){
        try {
            return this.mappingJackson2HttpMessageConverter.getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new TraceBizException("数据转换出错");
        }
    }
}
