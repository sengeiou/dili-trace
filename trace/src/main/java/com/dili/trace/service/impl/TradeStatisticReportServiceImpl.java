package com.dili.trace.service.impl;

import com.dili.trace.dao.TradeStatisticMapper;
import com.dili.trace.dto.BillSumReportDto;
import com.dili.trace.dto.CommodityWeightReportDto;
import com.dili.trace.service.TradeStatisticReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author asa.lee
 */
@Service
@EnableRetry
public class TradeStatisticReportServiceImpl implements TradeStatisticReportService {

    @Autowired
    private TradeStatisticMapper tradeStatisticMapper;

    @Override
    public List<CommodityWeightReportDto> getCommodityWeightReportList(CommodityWeightReportDto commodityWeightReportDto) {

        return tradeStatisticMapper.getCommodityWeightReportList(commodityWeightReportDto);
    }

    @Override
    public List<BillSumReportDto> getBillSumReportList(BillSumReportDto query) {
        return tradeStatisticMapper.getBillSumReportList(query);
    }

    @Override
    public List<BillSumReportDto> getMerchantReportList(BillSumReportDto query) {
        return tradeStatisticMapper.getMerchantReportList(query);
    }
}
