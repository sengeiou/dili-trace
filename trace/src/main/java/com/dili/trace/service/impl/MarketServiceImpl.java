package com.dili.trace.service.impl;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.component.RpcComponent;
import com.dili.trace.domain.Market;
import com.dili.trace.enums.MarketEnum;
import com.dili.trace.service.FirmRpcService;
import com.dili.trace.service.MarketService;
import com.dili.trace.service.WebCtxService;
import com.dili.uap.sdk.domain.Firm;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author Lily
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class MarketServiceImpl implements MarketService {
    @Autowired
    WebCtxService webCtxService;
    @Autowired
    RpcComponent rpcComponent;
    @Autowired
    FirmRpcService firmRpcService;

    @Override
    public Long getCurrentLoginMarketId() {
        return this.webCtxService.getCurrentFirm().map(Firm::getId).orElseThrow(() -> {
            return new TraceBizException("当前登录用户所属市场不存在");
        });
    }

    public Firm getCurrentMarket(){
        return this.webCtxService
                .getCurrentFirm()
                .orElseThrow(()-> new TraceBizException("当前登录用户所属市场不存在"));
    }

    /**
     * 从 UAP 获取市场列表
     * @return
     */
    @Override
    public List<Market> listFromUap() {
        List<Market> markets = rpcComponent.getMarketConfigs().orElse(new ArrayList<>());
        return markets;
    }

    /**
     * 从 UAP 获取市场编码映射
     * @return
     */
    @Override
    public Map<String, String> getMarketCodeMap() {
        Map<String, String> marketMap = rpcComponent.getMarketCodeMap().orElse(new HashedMap());
        return marketMap;
    }

    @Override
    public Firm getMarketByCode(MarketEnum marketEnum) {
        Map<String, String> marketCodeMap = this.getMarketCodeMap();
        if (marketCodeMap == null || marketCodeMap.size() == 0) {
            throw new TraceBizException("Code["+marketEnum.getCode()+"]对应的市场不存在");
        }
        String marketCode = marketCodeMap.get(marketEnum.getCode());
        return firmRpcService.getFirmByCode(marketCode).orElseThrow(() -> {
            return new TraceBizException("Code["+marketEnum.getCode()+"]对应的市场不存在");
        });
    }


    public Optional<Firm> getMarketById(Long marketId){
         return this.firmRpcService.getFirmById(marketId);
    }

}
