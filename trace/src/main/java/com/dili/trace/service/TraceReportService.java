package com.dili.trace.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.TraceReportDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;

@Service
public class TraceReportService {
    @Autowired
    RegisterBillMapper billMapper;

    public List<TraceReportDto> traceUserReport(RegisterBillDto query) {

        List<TraceReportDto> list = this.billMapper.userCountQuery();
        Map<String, TraceReportDto> areaReportDtoMap = StreamEx.of(list).map(item->{
            item.setBillCount(0);
            item.setTradeDetailBuyerCount(0);
            item.setGreenBillCount(0);
            item.setYellowBillCount(0);
            item.setRedBillCount(0);
            item.setNoVerifyedBillCount(0);
            return item;
        }).toMap(TraceReportDto::getArea,
                Function.identity());

        StreamEx.of(this.billMapper.billCountQuery()).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto()).setBillCount(item.getBillCount());
        });

        StreamEx.of(this.billMapper.tradeDetailBuyerCount()).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto()).setTradeDetailBuyerCount(item.getTradeDetailBuyerCount());
        });


        StreamEx.of(this.billMapper.greenBillCount()).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto()).setGreenBillCount(item.getGreenBillCount());
        });

        StreamEx.of(this.billMapper.yellowBillCount()).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto()).setYellowBillCount(item.getYellowBillCount());
        });

        StreamEx.of(this.billMapper.redBillCount()).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto()).setRedBillCount(item.getRedBillCount());
        });

        StreamEx.of(this.billMapper.noVerifyedBillCount()).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto()).setNoVerifyedBillCount(item.getNoVerifyedBillCount());
        });
        return list;

    }
}