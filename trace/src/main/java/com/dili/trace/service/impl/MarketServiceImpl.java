package com.dili.trace.service.impl;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Market;
import com.dili.trace.service.FirmRpcService;
import com.dili.trace.service.MarketService;
import com.dili.trace.service.WebCtxService;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.rpc.FirmRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;


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
            return new TraceBusinessException("当前登录用户所属市场不存在");
        });
//        System.out.println(out);
//        // 根据市场code查询市场id
//        Market query = new Market();
//        query.setCode(firmCode);
//        List<Market> markets = this.list(query);
//        if (CollectionUtils.isEmpty(markets)) {
//            throw new TraceBusinessException("市场【"+firmId+"】不存在");
//        }
//        return markets.get(0).getId();
    }
}
