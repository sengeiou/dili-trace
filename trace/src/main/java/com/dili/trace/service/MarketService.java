package com.dili.trace.service;

import com.dili.trace.domain.Market;
import com.dili.trace.enums.MarketEnum;
import com.dili.uap.sdk.domain.Firm;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Lily
 */
public interface MarketService {
    /**
     *
     * @Author guzman.liu
     * @Description
     * 获取当前市场的ID
     * @Date 2020/11/26 14:23
     */
    Long getCurrentLoginMarketId();

    /**
     *
     * @Author guzman.liu
     * @Description
     * 获取当前市场的所有信息
     * @Date 2020/11/26 14:23
     */
    public Firm getCurrentMarket();

    /**
     *
     * @Author Alvin.li
     * @Description
     * 获取uap溯源系统市场列表
     * ==>从数据字典获取，主要用于数据上报
     * @Date 2020/11/26 14:23
     */
    List<Market> listFromUap();

    /**
     *
     * @Author Alvin.li
     * @Description
     * 获取uap溯源系统市场映射
     * @Date 2020/11/26 14:23
     */
    Map<String, String> getMarketCodeMap();

    /**
     *
     * @Author Alvin.li
     * @Description
     * 根据市场编码获取市场信息
     * @Date 2020/11/26 14:23
     */
    Firm getMarketByCode(MarketEnum marketEnum);

    /**
     *根据Makeid获取市场
     * @Date 2020/11/26 17:07
     */
    Optional<Firm> getMarketById(Long marketId);

    /**
     *获取市场列表（已激活，非）
     * @Date 2020/11/26 17:07
     */
    Optional<List<Market>> getMarkets();
}
