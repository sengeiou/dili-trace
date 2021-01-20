/**
 * Copyright (C) DiliGroup. All Rights Reserved.
 * <p>
 * UserFlagProvider created on 2021/1/20 17:17 by Ron.Peng
 */
package com.dili.trace.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.enums.UserFlagEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 * Description
 * TODO file description here
 *
 * @author Ron.Peng
 * @since 1.0
 *
 * Change History
 * Date  Modifier  DESC.
 * 2021/1/20  Ron.Peng  Initial version.
 * </pre>
 */
@Component
public class UserFlagProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER = buildValuePair();
    private static List<ValuePair<?>> buildValuePair(){

        List<ValuePair<?>> list = new ArrayList<>();
        list.addAll(Stream.of(UserFlagEnum.values())
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
            Optional<UserFlagEnum> userFlagEnum = UserFlagEnum.fromCode(Integer.parseInt(object.toString()));
            return userFlagEnum.get().getName();
        }catch(Exception e){
            return null;
        }

    }
}
