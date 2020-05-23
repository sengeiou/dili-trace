package com.dili.trace.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.glossary.UserTypeEnum;

import org.springframework.stereotype.Component;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:44
 */
@Component
public class UserTypeEnumProvider implements ValueProvider {

    private static final List<ValuePair<?>>  BUFFER = buildValuePair();
	private static List<ValuePair<?>> buildValuePair(){
		
		List<ValuePair<?>> list = new ArrayList<>();
		list.addAll(Stream.of(UserTypeEnum.values())
                .map(e -> new ValuePairImpl<>(e.getDesc(), e.getCode().toString()))
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
            return "";
        }
        try{
            return UserTypeEnum.fromCode(Integer.parseInt(object.toString())).getDesc();
        }catch(Exception e){
            return null;
        }
     
    }
}
