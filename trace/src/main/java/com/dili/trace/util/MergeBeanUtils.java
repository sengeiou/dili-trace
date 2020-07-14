package com.dili.trace.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Predicate;

import com.google.common.collect.Maps;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

public class MergeBeanUtils {
    public static <T> T merge(Object src, T target, boolean ignoreSrcBlankValue) {
        return merge(src, target, p -> {
            // Object key = p.getLeft();
            Object srcValue = p.getMiddle();
            // Object targetValue = p.getRight();
            if (ignoreSrcBlankValue == true) {
                if (srcValue == null) {
                    return false;
                } else if (srcValue instanceof String) {
                    String value = String.valueOf(srcValue);
                    if (value.trim().length() == 0) {
                        return false;
                    }
                }
            }
            return true;
        });
    }

    public static <T> T merge(Object src, T target) {
        return merge(src, target, p -> true);
    }

    public static <T> T merge(Object src, T target, Predicate<Triple<Object, Object, Object>> keyValuesFilter) {
        Map<Object, Object> srcbm = Maps.newHashMap(new BeanMap(src));
        Map<Object, Object> targetbm = Maps.newHashMap(new BeanMap(target));
        for (Object key : srcbm.keySet()) {
            Object srcValue = srcbm.get(key);
            Object targetValue = targetbm.get(key);
            if (keyValuesFilter.test(MutableTriple.of(key, srcValue, targetValue))) {
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