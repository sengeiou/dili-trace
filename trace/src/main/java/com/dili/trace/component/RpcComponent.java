package com.dili.trace.component;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Market;
import com.dili.trace.enums.datadic.GovPlatformSysConfigEnum;
import com.dili.trace.enums.datadic.MarketMapSysConfigEnum;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Optional<List<Market>> getMarketConfigs() {
        List<DataDictionaryValue> dataDictionaryValues = this.listDataDictionaryValueByDdCode(GovPlatformSysConfigEnum.DD_CODE.getName());

        if (CollectionUtils.isEmpty(dataDictionaryValues)){
            return Optional.empty();
        }
        Map<String, Map<String, String>> collect = dataDictionaryValues.stream()
                .collect(Collectors.groupingBy(DataDictionaryValue::getFirmCode,Collectors.toMap(DataDictionaryValue::getName,DataDictionaryValue::getCode)));

        List<Market> marketList = collect.entrySet().stream().map(e -> {
            Map<String, String> value = e.getValue();
            Market market = new Market();
            String appId = value.get(GovPlatformSysConfigEnum.APP_ID.getName());
            String appSecret = value.get(GovPlatformSysConfigEnum.APP_SECRET.getName());
            String marketId = value.get(GovPlatformSysConfigEnum.MARKET_ID.getName());
            String url = value.get(GovPlatformSysConfigEnum.URL.getName());
            market.setPlatformMarketId(StringUtils.isEmpty(marketId) ? null : Long.valueOf(marketId));
            market.setAppId(StringUtils.isEmpty(appId) ? null : Long.valueOf(appId));
            market.setAppSecret(appSecret);
            market.setContextUrl(url);
            market.setCode(e.getKey().trim());
            return market;
        }).collect(Collectors.toList());

        return Optional.of(marketList);
    }

    /**
     * 获取UAP市场编译映射配置
     * @return
     */
    public Optional<Map<String, String>> getMarketCodeMap() {
        List<DataDictionaryValue> dataDictionaryValues = this.listDataDictionaryValueByDdCode(MarketMapSysConfigEnum.DD_CODE.getCode());
        if (CollectionUtils.isEmpty(dataDictionaryValues)) {
            return Optional.empty();
        }
        Map<String, String> marketCodeMap = new HashedMap();
        dataDictionaryValues.stream().forEach(d -> {
            marketCodeMap.put(d.getName(), d.getCode());
        });
        return Optional.of(marketCodeMap);
    }
}
