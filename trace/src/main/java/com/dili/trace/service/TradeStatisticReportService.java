package com.dili.trace.service;

import com.dili.trace.dto.BillSumReportDto;
import com.dili.trace.dto.CommodityWeightReportDto;

import java.util.List;

/**
 * @author asa.lee
 */
public interface TradeStatisticReportService{

    /**
     * 商品进场重量报告
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
     * 商戶进场重量报告
     * @param query
     * @return
     */
    List<BillSumReportDto> getMerchantReportList(BillSumReportDto query);
}
