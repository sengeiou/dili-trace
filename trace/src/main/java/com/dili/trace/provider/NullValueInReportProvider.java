package com.dili.trace.provider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dili.commons.glossary.YesOrNoEnum;
import org.springframework.stereotype.Component;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:44
 */
@Component
public class NullValueInReportProvider implements ValueProvider {

    private static final List<ValuePair<?>>BUFFER =buildValuePair();
	private static List<ValuePair<?>> buildValuePair(){
		
		List<ValuePair<?>> list = new ArrayList<>();
		list.addAll(Stream.of(YesOrNoEnum.values())
                .map(e -> new ValuePairImpl<>(e.getName(), e.getCode().toString()))
                .collect(Collectors.toList()));
		return list;
    }

 

    @Override
    public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
        return buildValuePair();
    }

    @Override
    public String getDisplayText(Object object, Map map, FieldMeta fieldMeta) {
        if (null == object) {
            return "-";
        }
        if(object instanceof BigDecimal) {
        	BigDecimal value=(BigDecimal) object;
        	return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
        }
        
        return String.valueOf(object);
    }
}
