package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.Market;
import com.dili.trace.enums.MarketEnum;
import com.dili.uap.sdk.domain.Firm;

import java.util.List;
import java.util.Map;

/**
 * @author Lily
 */
public interface MarketService extends BaseService<Market, Long> {
    Long getCurrentLoginMarketId();

    List<Market> listFromUap();

    Map<String, String> getMarketCodeMap();

    Firm getMarketByCode(MarketEnum marketEnum);
}
