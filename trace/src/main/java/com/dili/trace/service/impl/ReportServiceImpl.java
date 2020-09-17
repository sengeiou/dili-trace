package com.dili.trace.service.impl;

import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dao.TradeRequestMapper;
import com.dili.trace.dto.*;
import com.dili.trace.service.ReportService;
import com.dili.trace.util.CollectorsUtil;
import one.util.streamex.StreamEx;
import org.jooq.SelectSeekLimitStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

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
        List<ProductOrigionReportDto> list = registerBillMapper.queryProductOrigionReport(queryDto);
        Map<String, List<ProductOrigionReportDto>> map = list.stream().collect(Collectors.groupingBy(
                ProductOrigionReportDto::getProductName));
        List<ProductOrigionReportDto> finalList = new ArrayList<>();
        StreamEx.ofValues(map).forEach(mapList -> {
            if(mapList.size() >= 5)
            {
                finalList.addAll(mapList.subList(0,5));
            }
            else
            {
                finalList.addAll(mapList.subList(0,mapList.size()));
            }
        });
        Map<String, BigDecimal> finalMap = finalList.stream().collect(Collectors.groupingBy(
                ProductOrigionReportDto::getProductName, CollectorsUtil.summingBigDecimal(ProductOrigionReportDto::getWeight)));
        StreamEx.of(finalList).forEach(td -> {
            td.setTotalWeight(finalMap.get(td.getProductName()));
        });
        finalList.sort(comparing(ProductOrigionReportDto::getTotalWeight).
                thenComparing(ProductOrigionReportDto::getWeight).reversed());
        return finalList;
    }

    @Override
    public List<PurchaseGoodsReportDto> purchaseGoodsReportList(PurchaseGoodsReportQueryDto queryDto) {
        return tradeRequestMapper.queryPurchaseGoodsReportList(queryDto);
    }

    @Override
    public List<PurchaseGoodsReportDto> purchaseGoodsReportList(UserPurchaseReportQueryDto queryDto) {
        List<PurchaseGoodsReportDto> list = tradeRequestMapper.queryUserPurchaseReportList(queryDto);
        Map<String, BigDecimal> map = list.stream().collect(Collectors.groupingBy(
                PurchaseGoodsReportDto::getUserName, CollectorsUtil.summingBigDecimal(PurchaseGoodsReportDto::getWeight)));
        StreamEx.of(list).forEach(td -> {
            td.setTotalWeight(map.get(td.getUserName()));
        });
        list.sort(comparing(PurchaseGoodsReportDto::getTotalWeight).
                thenComparing(PurchaseGoodsReportDto::getWeight).reversed());
        return list;
    }


}
