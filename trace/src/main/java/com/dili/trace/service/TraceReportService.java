package com.dili.trace.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dto.TraceReportDto;
import com.dili.trace.dto.TraceReportQueryDto;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import liquibase.pro.packaged.t;
import one.util.streamex.StreamEx;

@Service
public class TraceReportService {
    @Autowired
    RegisterBillMapper billMapper;

    public Map<String, TraceReportDto> getTraceBillReportData(TraceReportQueryDto query) {
        query.setGreenBillVerifyStatus(Lists.newArrayList(BillVerifyStatusEnum.PASSED.getCode()));
        query.setRedBillVerifyStatus(Lists.newArrayList(BillVerifyStatusEnum.NO_PASSED.getCode()));
        query.setYellowBillVerifyStatus(
                Lists.newArrayList(BillVerifyStatusEnum.NONE.getCode(), BillVerifyStatusEnum.RETURNED.getCode()));
        query.setNoneVerifyStatus(Lists.newArrayList(BillVerifyStatusEnum.NONE.getCode()));

        List<TraceReportDto> list = this.billMapper.userCountQuery(query);
        Map<String, TraceReportDto> areaReportDtoMap = StreamEx.of(list).map(item -> {
            item.setBillCount(0);
            item.setTradeDetailBuyerCount(0);
            item.setGreenBillCount(0);
            item.setYellowBillCount(0);
            item.setRedBillCount(0);
            item.setNoVerifyedBillCount(0);
            return item;
        }).toMap(TraceReportDto::getArea, Function.identity());

        StreamEx.of(this.billMapper.billCountQuery(query)).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto()).setBillCount(item.getBillCount());
        });

        StreamEx.of(this.billMapper.tradeDetailBuyerCount(query)).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
                    .setTradeDetailBuyerCount(item.getTradeDetailBuyerCount());
        });

        StreamEx.of(this.billMapper.greenBillCount(query)).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
                    .setGreenBillCount(item.getGreenBillCount());
        });

        StreamEx.of(this.billMapper.yellowBillCount(query)).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
                    .setYellowBillCount(item.getYellowBillCount());
        });

        StreamEx.of(this.billMapper.redBillCount(query)).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
                    .setRedBillCount(item.getRedBillCount());
        });

        StreamEx.of(this.billMapper.noVerifyedBillCount(query)).forEach(item -> {
            areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
                    .setNoVerifyedBillCount(item.getNoVerifyedBillCount());
        });

        TraceReportDto total = StreamEx.of(list).map(item -> {

            item.calculatePercentage();
            return item;
        }).reduce((t, v) -> {
            t.setBillCount(v.getBillCount());
            t.setTradeDetailBuyerCount(v.getTradeDetailBuyerCount());
            t.setGreenBillCount(v.getGreenBillCount());
            t.setYellowBillCount(v.getYellowBillCount());
            t.setRedBillCount(v.getRedBillCount());
            t.setNoVerifyedBillCount(v.getNoVerifyedBillCount());
            return t;
        }).orElseGet(() -> {
            TraceReportDto dto = new TraceReportDto();
            dto.setBillCount(0);
            dto.setTradeDetailBuyerCount(0);
            dto.setGreenBillCount(0);
            dto.setYellowBillCount(0);
            dto.setRedBillCount(0);
            dto.setNoVerifyedBillCount(0);
            return dto;
        });
        total.calculatePercentage();
        areaReportDtoMap.put("Total", total);
        return areaReportDtoMap;

    }
}