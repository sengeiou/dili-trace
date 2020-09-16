package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.dto.BillSumReportDto;
import com.dili.trace.dto.CommodityWeightReportDto;

import java.util.List;

/**
 * @author asa.lee
 */
public interface TradeStatisticMapper extends MyMapper<CommodityWeightReportDto> {

    /**
     * 商品进场重量报表
     *
     * @param commodityWeightReportDto
     * @return
     */
    List<CommodityWeightReportDto> getCommodityWeightReportList(CommodityWeightReportDto commodityWeightReportDto);

    /**
     * 报备进场汇总统计
     * @param query
     * @return
     */
    List<BillSumReportDto> getBillSumReportList(BillSumReportDto query);

    /**
     * 商戶进场重量报表
     * @param query
     * @return
     */
    List<BillSumReportDto> getMerchantReportList(BillSumReportDto query);
}
