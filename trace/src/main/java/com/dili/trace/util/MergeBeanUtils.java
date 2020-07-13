package com.dili.trace.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;

import one.util.streamex.StreamEx;

public class MergeBeanUtils {
    public static <T> T merge(Object src, T target) {
        return merge(src, target, null);
    }
    public static <T> T merge(Object src, T target, String[] ignoreProperties) {
        List<String> keyList = StreamEx.ofNullable(ignoreProperties).nonNull().flatArray(item -> item).nonNull()
                .distinct().toList();
        Map<Object, Object> srcbm = Maps.newHashMap(new BeanMap(src));
        Map<Object, Object> targetbm = Maps.newHashMap(new BeanMap(target));
        for (Object key : srcbm.keySet()) {
            if (keyList.contains(key)) {
                continue;
            }
            Object srcValue = srcbm.get(key);
            if (srcValue instanceof String) {
                srcValue = StringUtils.trimToNull(srcValue.toString());
            }
            Object targetValue = targetbm.get(key);
            if (targetValue instanceof String) {
                targetValue = StringUtils.trimToNull(targetValue.toString());
            }
            if (targetValue == null && srcValue != null) {
                targetbm.put(key, srcValue);
            }
        }
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(target, targetbm);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return target;
    }
}