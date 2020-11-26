package com.dili.trace.service.impl;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.Market;
import com.dili.trace.service.MarketService;
import com.dili.trace.service.WebCtxService;
import com.dili.uap.sdk.domain.Firm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Lily
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class MarketServiceImpl extends BaseServiceImpl<Market, Long> implements MarketService {
    @Autowired
    WebCtxService webCtxService;

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
}
