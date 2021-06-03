package com.dili.trace.dao;

import java.math.BigDecimal;
import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.output.TradeDetailBillOutput;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.RegisterBillDto;
import org.apache.ibatis.annotations.Param;

public interface TradeDetailMapper extends MyMapper<TradeDetail> {
    /**
     * 查询用户的登记单类型
     */
    public List<TradeDetailBillOutput> selectTradeDetailAndBill(RegisterBillDto dto);


    /**
     * 分组查询统计重量及检测结果
     *
     * @param tradeDetail
     * @return
     */
    public List<TradeDetail> groupSumWeightByProductStockId(TradeDetail tradeDetail);
}