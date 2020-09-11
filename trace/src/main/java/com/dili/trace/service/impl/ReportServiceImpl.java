package com.dili.trace.service.impl;

import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dao.TradeRequestMapper;
import com.dili.trace.dto.*;
import com.dili.trace.service.ReportService;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableRetry
public class ReportServiceImpl implements ReportService{


    @Autowired
    private RegisterBillMapper registerBillMapper;

    @Autowired
    private TradeRequestMapper tradeRequestMapper;

    @Override
    public List<OrigionReportDto> origionReportList(OrigionReportQueryDto queryDto) {
        return registerBillMapper.queryOrigionReport(queryDto);
    }

    @Override
    public List<ProductOrigionReportDto> productOrigionReportList(OrigionReportQueryDto queryDto) {
        return registerBillMapper.queryProductOrigionReport(queryDto);
    }

    @Override
    public List<PurchaseGoodsReportDto> purchaseGoodsReportList(PurchaseGoodsReportQueryDto queryDto) {
        return tradeRequestMapper.queryPurchaseGoodsReportList(queryDto);
    }

    @Override
    public List<PurchaseGoodsReportDto> purchaseGoodsReportList(UserPurchaseReportQueryDto queryDto) {
        List<PurchaseGoodsReportDto> list = tradeRequestMapper.queryUserPurchaseReportList(queryDto);
        /*Map<String,UserPurchaseReportQueryDto> map = list.stream().collect(Collectors.groupingBy(
               dto -> new PurchaseGoodsReportDto(dto.userName), Collectors.summarizingDouble(dto -> dto.weight.doubleValue())));

*/
        return list;
    }


}
