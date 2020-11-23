package com.dili.sg.trace.provider;

import com.dili.sg.trace.glossary.YnEnum;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	private List<ValuePair<?>> buildValuePair() {
		return Stream.of(YnEnum.values())
				.map(e -> new ValuePairImpl<>(e.getDesc(), e.getCode().toString())).collect(Collectors.toList());

	}
   

    @Override
    public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
        return buildValuePair() ;
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
