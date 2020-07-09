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
import com.dili.trace.enums.PreserveTypeEnum;

import org.springframework.stereotype.Component;

@Component
public class PreserveTypeProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {

        List<ValuePair<?>> list = new ArrayList<>();
        list.addAll(Stream.of(PreserveTypeEnum.values())
                .map(e -> new ValuePairImpl<>(e.getName(), e.getCode().toString())).collect(Collectors.toList()));
        return list;
    }

    @Override
    public String getDisplayText(Object object, Map metaMap, FieldMeta fieldMeta) {
        if (null == object) {
            return null;
        }
       String text= Stream.of(PreserveTypeEnum.values()).filter(val -> object.toString().equals( val.getCode().toString())).findFirst().map(PreserveTypeEnum::getName).orElse("");
        return text;
    }

}