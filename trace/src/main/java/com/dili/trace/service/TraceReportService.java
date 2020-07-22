package com.dili.trace.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.dili.trace.dao.CheckinOutRecordMapper;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dto.TraceReportDto;
import com.dili.trace.dto.TraceReportQueryDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;

@Service
public class TraceReportService {
    @Autowired
    RegisterBillMapper billMapper;
    @Autowired
    CheckinOutRecordMapper checkinOutRecordMapper;

    public Map<String, TraceReportDto> getTraceBillReportData(TraceReportQueryDto query) {
        query.setGreenBillVerifyStatus(Lists.newArrayList(BillVerifyStatusEnum.PASSED.getCode()));
        query.setRedBillVerifyStatus(Lists.newArrayList(BillVerifyStatusEnum.NO_PASSED.getCode()));
        query.setYellowBillVerifyStatus(
                Lists.newArrayList(BillVerifyStatusEnum.NONE.getCode(), BillVerifyStatusEnum.RETURNED.getCode()));
        query.setNoneVerifyStatus(Lists.newArrayList(BillVerifyStatusEnum.NONE.getCode()));


        List<TraceReportDto> list =  this.billMapper.selectBillReportData(query);

        Map<String, TraceReportDto> areaReportDtoMap = StreamEx.of(list).map(item -> {
            item.sum(new TraceReportDto());
            return item;
        }).toMap(TraceReportDto::getGroupKey, Function.identity());

        // StreamEx.of(this.billMapper.billCountQuery(query)).forEach(item -> {
        //     areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto()).setBillCount(item.getBillCount());
        // });

        // StreamEx.of(this.billMapper.tradeDetailBuyerCount(query)).forEach(item -> {
        //     areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
        //             .setTradeDetailBuyerCount(item.getTradeDetailBuyerCount());
        // });

        // StreamEx.of(this.billMapper.greenBillCount(query)).forEach(item -> {
        //     areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
        //             .setGreenBillCount(item.getGreenBillCount());
        // });

        // StreamEx.of(this.billMapper.yellowBillCount(query)).forEach(item -> {
        //     areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
        //             .setYellowBillCount(item.getYellowBillCount());
        // });

        // StreamEx.of(this.billMapper.redBillCount(query)).forEach(item -> {
        //     areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
        //             .setRedBillCount(item.getRedBillCount());
        // });

        // StreamEx.of(this.billMapper.noVerifyedBillCount(query)).forEach(item -> {
        //     areaReportDtoMap.getOrDefault(areaReportDtoMap, new TraceReportDto())
        //             .setNoVerifyedBillCount(item.getNoVerifyedBillCount());
        // });

        TraceReportDto total = StreamEx.of(list).map(item -> {
            item.calculatePercentage();
            return item;
        }).reduce(this.defaultReportDTO(),(t, v) -> {
            t.sum(v);
            return t;
        });
        total.calculatePercentage();
        areaReportDtoMap.put("Total", total);
        return areaReportDtoMap;

    }

    public Map<String,TraceReportDto> getCommonCheckinReportData(TraceReportQueryDto query) {
        query.setBillType(BillTypeEnum.NONE.getCode());
        List<TraceReportDto>list=this.checkinOutRecordMapper.groupCountCommonBillByColor(query);
        Map<String,TraceReportDto>mapData= StreamEx.ofNullable(list).nonNull().flatCollection(Function.identity()).toMap(TraceReportDto::getGroupKey, Function.identity());
        TraceReportDto total = StreamEx.of(list).map(item -> {
            item.calculatePercentage();
            return item;
        }).reduce(this.defaultReportDTO(),(t, v) -> {
           t.sum(v);
            return t;
        });
        mapData.put("Total", total);
        return mapData;
    }

    public TraceReportDto getSupplementCheckinReportData(TraceReportQueryDto query) {
        query.setBillType(BillTypeEnum.SUPPLEMENT.getCode());
        return StreamEx.ofNullable(this.checkinOutRecordMapper.groupCountSupplementBillByColor(query)).nonNull()
                .flatCollection(Function.identity()).nonNull().findFirst().orElseGet(() -> {

                    return this.defaultReportDTO();
                });

    }

    private TraceReportDto defaultReportDTO() {
        TraceReportDto dto = new TraceReportDto();
        dto.setBillCount(0);
        dto.setTradeDetailBuyerCount(0);
        dto.setGreenBillCount(0);
        dto.setYellowBillCount(0);
        dto.setRedBillCount(0);
        dto.setNoVerifyedBillCount(0);
        return dto;
    }
}