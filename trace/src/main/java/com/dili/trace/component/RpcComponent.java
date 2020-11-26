package com.dili.trace.component;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.assets.sdk.rpc.CityRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Market;
import com.dili.trace.enums.datadic.GovPlatformSysConfigEnum;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Guzman
 * @version 1.0
 * @ClassName RpcComponent
 * @Description
 * RPC公共调用类
 * @createTime 2020年11月26日 09:40:00
 */
@Component
public class RpcComponent {
    private static final Logger logger = LoggerFactory.getLogger(RpcComponent.class);

    @Autowired(required = false)
    DataDictionaryRpc dataDictionaryRpc;

    /**
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/26 10:00
     */
    public List<DataDictionaryValue> listDataDictionaryValueByDdCode(String ddCode){
        try {
            BaseOutput<List<DataDictionaryValue>> out = dataDictionaryRpc.listDataDictionaryValueByDdCode(ddCode);
            if (!out.isSuccess()) {
                return Lists.newArrayList();
            }
            return StreamEx.ofNullable(out.getData()).nonNull()
                    .flatCollection(Function.identity()).nonNull().toList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Lists.newArrayList();
        }
    }


    /**
     *
     * @Author guzman.liu
     * @Description
     * 获取上传给融食安系统的市场的配置
     * @Date 2020/11/26 10:57
     */
    public Optional<List<Market>> getMarketConfigs(){
        List<DataDictionaryValue> dataDictionaryValues = this.listDataDictionaryValueByDdCode(GovPlatformSysConfigEnum.DD_CODE.getName());

        if (CollectionUtils.isEmpty(dataDictionaryValues)){
            return Optional.empty();
        }
        Map<String, Map<String, String>> collect = dataDictionaryValues.stream()
                .collect(Collectors.groupingBy(DataDictionaryValue::getFirmCode,Collectors.toMap(DataDictionaryValue::getName,DataDictionaryValue::getCode)));

        List<Market> marketList = collect.values().stream().map(e -> {
            Market market = new Market();
            String appId = e.get(GovPlatformSysConfigEnum.APP_ID.getName());
            String appSecret = e.get(GovPlatformSysConfigEnum.APP_SECRET.getName());
            String marketId = e.get(GovPlatformSysConfigEnum.MARKET_ID.getName());
            String url = e.get(GovPlatformSysConfigEnum.URL.getName());
            market.setPlatformMarketId(StringUtils.isEmpty(marketId) ? null : Long.valueOf(marketId));
            market.setAppId(StringUtils.isEmpty(appId) ? null : Long.valueOf(appId));
            market.setAppSecret(appSecret);
            market.setContextUrl(url);
            return market;
        }).collect(Collectors.toList());

        return Optional.of(marketList);
    }
}
