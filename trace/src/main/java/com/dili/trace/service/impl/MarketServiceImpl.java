package com.dili.trace.service.impl;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.Market;
import com.dili.trace.service.MarketService;
import com.dili.uap.sdk.session.SessionContext;
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
    @Override
    public Long getCurrentLoginMarketId() {
        String marketCode = SessionContext.getSessionContext().getUserTicket().getFirmCode();
        // 根据市场code查询市场id
        Market query = new Market();
        query.setCode(marketCode);
        List<Market> markets = this.list(query);
        if (CollectionUtils.isEmpty(markets)) {
            throw new TraceBusinessException("市场【"+marketCode+"】不存在");
        }
        return markets.get(0).getId();
    }
}
