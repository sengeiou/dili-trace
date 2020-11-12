package com.dili.trace.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.enums.CheckReportEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 *
 * @author Ron.Peng
 * @date 2020/11/10
 */
@Component
public class CheckReportProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER = buildValuePair();
    private static List<ValuePair<?>> buildValuePair(){

        List<ValuePair<?>> list = new ArrayList<>();
        list.addAll(Stream.of(CheckReportEnum.values())
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
            return "";
        }
        try{
            return CheckReportEnum.fromCode(Integer.parseInt(object.toString())).get().getName();
        }catch(Exception e){
            return null;
        }

    }
}
