package com.dili.trace.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.enums.UserActiveEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author asa.lee
 */
@Component
public class UserActiveEnumProvider implements ValueProvider {

    private static final List<ValuePair<?>> BUFFER = buildValuePair();

    private static List<ValuePair<?>> buildValuePair() {

        List<ValuePair<?>> list = new ArrayList<>();
        list.addAll(Stream.of(UserActiveEnum.values())
                .map(e -> new ValuePairImpl<>(e.getName(), e.getCode().toString())).collect(Collectors.toList()));
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
        try {
            return UserActiveEnum.fromCode(Integer.parseInt(object.toString())).map(UserActiveEnum::getName).orElse("");
        } catch (Exception e) {
            return null;
        }

    }
}
