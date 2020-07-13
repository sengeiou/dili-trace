package com.dili.streamtest;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.dili.trace.domain.RegisterBill;
import com.google.common.collect.Maps;

import org.apache.commons.beanutils.BeanMap;
import org.junit.jupiter.api.Test;
import org.nutz.json.Json;

public class BeanUtilsTest {
    @Test
    public void dd() {
        RegisterBill src = new RegisterBill();
        src.setId(1L);
        src.setCode("abc");
        RegisterBill target = new RegisterBill();
        target.setId(2L);
        target.setCode("def");
        System.out.println(Json.toJson(src));
        System.out.println(Json.toJson(target));

        BeanMap srcbm = new BeanMap(src);
        Map<Object, Object> targetbm = Maps.newHashMap(new BeanMap(target));
        for (Object key : srcbm.keySet()) {
            Object value = srcbm.get(key);
            if (value != null) {
                targetbm.put(key, value);
            }
        }
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(target, targetbm);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(Json.toJson(src));
        System.out.println(Json.toJson(target));


    }
}