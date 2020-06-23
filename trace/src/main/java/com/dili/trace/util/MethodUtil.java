package com.dili.trace.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dili.trace.domain.RegisterBill;
import com.github.hervian.reflection.Types;
import com.github.hervian.reflection.Types.Consumer;
import com.google.common.collect.Maps;

import one.util.streamex.StreamEx;

public class MethodUtil {
	public static <T1> Map<String, Object> buildMap(T1 t1, Consumer<T1>... consumers) {
		if (consumers != null) {
			Map<String, Object> resultMap = Maps.newHashMap();
			StreamEx.of(consumers).nonNull().map(consumer -> {
				Method m = Types.createMethod(consumer);
				return m;
			}).mapToEntry(m -> {

				String getterName = m.getName();
				return StringUtils.uncapitalize(getterName.replace("get", ""));

			}, m -> {
				try {
					return m.invoke(t1, new Object[0]);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					return null;
				}
			}).forEach(e -> {
				String key = e.getKey();
				Object value = e.getValue();
				resultMap.put(key, value);
			});
			return resultMap;
		}
		return Maps.newHashMap();

	}
	public static <T1> Map<String, Object> replaceKey(Map<String, Object>map,Consumer<T1> consumer,String newKey) {
				String key= StringUtils.uncapitalize(Types.getName(consumer).replace("get", ""));
				if(map.containsKey(key)) {
					Object value=map.remove(key);
					map.put(newKey, value);
				}
				return map;
				
	}

	public static void main(String[] args) {
		RegisterBill bill = new RegisterBill();
		bill.setId(1L);
		bill.setName("ss");
		Map<String, Object> map = MethodUtil.buildMap(bill, RegisterBill::getId, RegisterBill::getName,RegisterBill::getCode);
		map=MethodUtil.replaceKey(map, RegisterBill::getId, "billId");
		System.out.println(map);
		String str=JSON.toJSONString(map,SerializerFeature.WriteMapNullValue);
		System.out.println(str);
	}
}
