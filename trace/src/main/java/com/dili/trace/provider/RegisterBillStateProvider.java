package com.dili.trace.provider;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:44
 */
@Component
public class RegisterBillStateProvider implements ValueProvider {

	private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

	static {
		BUFFER.addAll(Stream.of(RegisterBillStateEnum.values())
				.map(e -> new ValuePairImpl<>(e.getName(), e.getCode().toString())).collect(Collectors.toList()));
	}

	@Override
	public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
		Object queryParamsObj = map.get(ValueProvider.QUERY_PARAMS_KEY);
//		String emptyText = ValueProvider.EMPTY_ITEM_TEXT;
//		List<ValuePair<?>> valuePairs =  providerObj == null ? Collections.EMPTY_LIST : providerObj.getLookupList(val, paramMap, null);
//        valuePairs = Lists.newArrayList(valuePairs);
		if (queryParamsObj != null) {
//			//获取查询参数
			JSONObject queryParams = JSONObject.parseObject(queryParamsObj.toString());
			if(queryParams.containsKey("excludes")) {
				List<Object>excludes=(List<Object>) queryParams.get("excludes");
				if(excludes!=null&&!excludes.isEmpty()) {
					List<String>excludeKeys=excludes.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());
					
					return BUFFER.stream()
							.filter(val -> !excludeKeys.contains(val.getValue()))
							.collect(Collectors.toList());	
				}
				
			}
		}
		return BUFFER;
	}

	@Override
	public String getDisplayText(Object object, Map map, FieldMeta fieldMeta) {
		if (null == object) {
			return null;
		}
		ValuePair<?> valuePair = BUFFER.stream().filter(val -> object.toString().equals(val.getValue())).findFirst()
				.orElse(null);
		if (null != valuePair) {
			return valuePair.getText();
		}
		return null;
	}
}
