package com.dili.trace.controller;


import cn.hutool.core.util.NumberUtil;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.ValueProviderUtils;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provider转换
 */
@RestController
@RequestMapping({"/providerConvert"})
public class ProviderConvertController extends AbstractBaseController {
    @Autowired
    private ValueProviderUtils valueProviderUtils;

    public ProviderConvertController() {
    }

    /**
     * 转换
     * @param queryMap
     * @return
     */
    @PostMapping({"/list.action"})
    public List<ValuePair<?>> getLookupList(@RequestBody Map<String, Object> queryMap) {
        String provider = queryMap.get("provider").toString();
        boolean typeString = StreamEx.ofNullable(queryMap.get("stringValue")).map(String::valueOf).map(stringValue -> {
            try {
                return Boolean.parseBoolean(stringValue);
            } catch (Exception e) {
                return false;
            }
        }).findFirst().orElse(false);

        List result = new ArrayList();
        queryMap.remove("provider");

        Object queryParamsObj = queryMap.get(ValueProvider.QUERY_PARAMS_KEY);
        if (queryParamsObj != null) {
            queryMap.put(ValueProvider.QUERY_PARAMS_KEY, super.toJSONString(queryParamsObj));
        }
        List<ValuePair<?>> value = this.valueProviderUtils.getLookupList(provider, queryMap.get("value"), queryMap);
        value.forEach((valuePair) -> {
            if (NumberUtil.isNumber(valuePair.getValue().toString()) && !typeString) {
                result.add(new ValuePairImpl(valuePair.getText(), Long.valueOf(valuePair.getValue().toString())));
            } else {
                result.add(valuePair);
            }

        });
        return result;
    }
}
