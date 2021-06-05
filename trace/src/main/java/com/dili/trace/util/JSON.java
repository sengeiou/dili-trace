package com.dili.trace.util;

import com.dili.common.exception.TraceBizException;
import com.dili.trace.domain.RegisterBill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.HashMap;
import java.util.List;

public class JSON {
    public static String toJSONString(Object object) {
        ObjectMapper mapper = getOrCreateObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new TraceBizException("数据转换出错");
        }

    }

    public static List<?> parseArray(String str) {
        ObjectMapper mapper = getOrCreateObjectMapper();
        try {
            return mapper.readValue(str, new TypeReference<List<Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new TraceBizException("数据转换出错");
        }
    }

    public static void main(String[] args) {
        System.out.println(JSON.parseObject("{\"id\":23}", RegisterBill.class).getBillId());
    }

    public static <T> List<T> parseArray(String str, Class<T> clz) {

        ObjectMapper mapper = getOrCreateObjectMapper();
        try {
            return mapper.readValue(str, new TypeReference<List<T>>() {
            });
        } catch (JsonProcessingException e) {
            throw new TraceBizException("数据转换出错");
        }

    }

    public static <T> T parseObject(String str, Class<T> clz) {
        ObjectMapper mapper = getOrCreateObjectMapper();
        try {
            return mapper.readValue(str, clz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new TraceBizException("数据转换出错");
        }

    }

    private static ObjectMapper getOrCreateObjectMapper() {
        if (SpringbootUtil.getApplicationContext() != null) {
            ApplicationContext ctx = SpringbootUtil.getApplicationContext();
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = ctx.getBean(MappingJackson2HttpMessageConverter.class);
            if (mappingJackson2HttpMessageConverter != null) {
                return mappingJackson2HttpMessageConverter.getObjectMapper();
            }
        }
        return new ObjectMapper();
    }
}
