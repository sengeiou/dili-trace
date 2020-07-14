package com.dili.trace.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dili.common.exception.TraceBusinessException;
import com.dili.trace.api.output.TraceDataDto;
import com.dili.trace.api.output.TraceDetailOutputDto;
import com.dili.trace.domain.ProductStore;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.TradeTypeEnum;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;

@Service
public class BillTraceService {
    @Resource
    private UserService userService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    CheckinOutRecordService checkinOutRecordService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    UpStreamService upStreamService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    TradeRequestService tradeRequestService;
    @Autowired
    BatchStockService batchStockService;
    @Autowired
    ImageCertService imageCertService;

    public TraceDetailOutputDto viewBillTrace(Long tradeRequestid, Long userId) {
        TradeRequest tradeRequestItem = this.tradeRequestService.get(tradeRequestid);
        if (tradeRequestItem == null) {
            throw new TraceBusinessException("没有查找到详情");
        }

        TraceDetailOutputDto traceDetailOutputDto = new TraceDetailOutputDto();
        traceDetailOutputDto.setTradeRequestId(tradeRequestItem.getTradeRequestId());
        traceDetailOutputDto.setCreated(tradeRequestItem.getCreated());
        if (tradeRequestItem.getBuyerId().equals(userId)) {

            User seller = this.userService.get(tradeRequestItem.getSellerId());
            TraceDataDto upTrace = new TraceDataDto();
            upTrace.setCreated(tradeRequestItem.getCreated());
            upTrace.setBuyerName(tradeRequestItem.getBuyerName());
            upTrace.setSellerName(tradeRequestItem.getSellerName());
            upTrace.setMarketName(seller.getMarketName());
            upTrace.setTallyAreaNo(seller.getTallyAreaNos());

            TradeDetail tradeDetailQuery = new TradeDetail();
            tradeDetailQuery.setBuyerId(userId);
            tradeDetailQuery.setTradeRequestId(tradeRequestItem.getId());
            List<Long> parentIdList = StreamEx.of(this.tradeDetailService.listByExample(tradeDetailQuery))
                    .map(TradeDetail::getId).toList();
            List<TradeDetail> buyerTradeDetailList = this.tradeDetailService
                    .findTradeDetailByParentIdList(parentIdList);

            List<TraceDataDto> downTraceList = StreamEx.of(buyerTradeDetailList).map(TradeDetail::getTradeRequestId)
                    .nonNull().distinct().map(requestId -> {
                        TradeRequest tr = this.tradeRequestService.get(requestId);
                        User buyer = this.userService.get(tr.getBuyerId());
                        TraceDataDto downTrace = new TraceDataDto();
                        downTrace.setCreated(tr.getCreated());
                        downTrace.setBuyerName(tr.getBuyerName());
                        downTrace.setSellerName(tr.getSellerName());
                        downTrace.setMarketName(buyer.getMarketName());
                        downTrace.setTallyAreaNo(buyer.getTallyAreaNos());
                        return downTrace;
                    }).toList();
            traceDetailOutputDto.setUpTraceList(Lists.newArrayList(upTrace));
            traceDetailOutputDto.setDownTraceList(downTraceList);
        } else if (tradeRequestItem.getSellerId().equals(userId)) {

            TradeDetail condition = new TradeDetail();
            condition.setSellerId(userId);
            condition.setTradeRequestId(tradeRequestItem.getId());
            List<Long> upTradeDetailIdList = StreamEx.of(this.tradeDetailService.listByExample(condition))
                    .map(TradeDetail::getParentId).nonNull().distinct().toList();
            List<TraceDataDto> upTraceList = StreamEx
                    .of(this.tradeDetailService.findTradeDetailByIdList(upTradeDetailIdList))
                    .map(TradeDetail::getTradeRequestId).nonNull().distinct().map(requestId -> {
                        TradeRequest tr = this.tradeRequestService.get(requestId);
                        User buyer = this.userService.get(tr.getBuyerId());
                        TraceDataDto upTraceDto = new TraceDataDto();
                        upTraceDto.setCreated(tr.getCreated());
                        upTraceDto.setBuyerName(tr.getBuyerName());
                        upTraceDto.setSellerName(tr.getSellerName());
                        upTraceDto.setMarketName(buyer.getMarketName());
                        upTraceDto.setTallyAreaNo(buyer.getTallyAreaNos());
                        return upTraceDto;
                    }).nonNull().toList();

            // User seller = this.userService.get(tradeRequestItem.getSellerId());

            TradeDetail tradeDetailQuery = new TradeDetail();
            tradeDetailQuery.setSellerId(userId);
            tradeDetailQuery.setTradeRequestId(tradeRequestItem.getId());
            // List<Long> parentIdList =
            // StreamEx.of(this.tradeDetailService.listByExample(tradeDetailQuery))
            // .map(TradeDetail::getId).toList();
            // List<TradeDetail> buyerTradeDetailList = this.tradeDetailService
            // .findTradeDetailByParentIdList(parentIdList);

            List<TradeDetail> buyerTradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);
            List<TraceDataDto> downTraceList = StreamEx.of(buyerTradeDetailList).map(TradeDetail::getTradeRequestId)
                    .distinct().map(requestId -> {

                        TradeRequest tr = this.tradeRequestService.get(requestId);
                        User buyer = this.userService.get(tr.getBuyerId());
                        if (requestId == null || tr == null || buyer == null) {
                            return null;
                        }
                        TraceDataDto downTrace = new TraceDataDto();
                        downTrace.setCreated(tr.getCreated());
                        downTrace.setBuyerName(tr.getBuyerName());
                        downTrace.setSellerName(tr.getSellerName());
                        downTrace.setMarketName(buyer.getMarketName());
                        downTrace.setTallyAreaNo(buyer.getTallyAreaNos());
                        return downTrace;
                    }).nonNull().toList();
            traceDetailOutputDto.setUpTraceList(upTraceList);
            traceDetailOutputDto.setDownTraceList(downTraceList);
        } else {
            throw new TraceBusinessException("没有查询到数据");
        }
        ProductStore batchStockItem = this.batchStockService.get(tradeRequestItem.getProductStockId());
        traceDetailOutputDto.setBrandName(batchStockItem.getBrandName());
        traceDetailOutputDto.setSpecName(batchStockItem.getSpecName());
        traceDetailOutputDto.setProductName(batchStockItem.getProductName());
        TradeDetail query = new TradeDetail();
        query.setTradeRequestId(tradeRequestItem.getId());
        List<TradeDetail> traceList = this.tradeDetailService.listByExample(query);
        List<Long> billIdList = StreamEx.of(traceList).map(TradeDetail::getBillId).distinct().toList();
        List<ImageCert> imageCertList = this.imageCertService.findImageCertListByBillIdList(billIdList);
        traceDetailOutputDto.setImageCertList(imageCertList);

        return traceDetailOutputDto;

    }

    public List<TradeDetail> viewTradeDetailList(Long tradeRequestId, Long userId) {

        TradeRequest tradeRequestItem = this.tradeRequestService.get(tradeRequestId);
        if (tradeRequestItem == null) {
            throw new TraceBusinessException("没有查找到详情");
        }
        List<TradeDetail> list =Lists.newArrayList();
        if (tradeRequestItem.getBuyerId().equals(userId)) {

            TradeDetail tradeDetailQuery = new TradeDetail();
            tradeDetailQuery.setBuyerId(userId);
            tradeDetailQuery.setTradeRequestId(tradeRequestItem.getId());
            List<TradeDetail> downTraceList = this.tradeDetailService.listByExample(tradeDetailQuery);
            list.addAll(downTraceList);
        } else if (tradeRequestItem.getSellerId().equals(userId)) {

            TradeDetail condition = new TradeDetail();
            condition.setSellerId(userId);
            condition.setTradeRequestId(tradeRequestItem.getId());
            List<Long> upTradeDetailIdList = StreamEx.of(this.tradeDetailService.listByExample(condition))
                    .map(TradeDetail::getParentId).nonNull().distinct().toList();

            TradeDetail tradeDetailQuery = new TradeDetail();
            tradeDetailQuery.setSellerId(userId);
            tradeDetailQuery.setTradeRequestId(tradeRequestItem.getId());

            List<TradeDetail> buyerTradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);
            list.addAll(buyerTradeDetailList); ;
           
        } else {
            throw new TraceBusinessException("没有查询到数据");
        }
        
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setTradeRequestId(tradeRequestItem.getTradeRequestId());
        List<Long> billIdList = StreamEx.of(list).filter(td -> {
            return TradeTypeEnum.NONE.equalsToCode(td.getTradeType());
        }).map(TradeDetail::getBillId).distinct().toList();

        Map<Long, String> billIdPlateMap = StreamEx.ofNullable(billIdList).filter(li -> !li.isEmpty()).flatMap(li -> {
            RegisterBillDto dto = new RegisterBillDto();
            dto.setIdList(li);
            return StreamEx.of(this.registerBillService.listByExample(dto));
        }).toMap(RegisterBill::getId, RegisterBill::getPlate);
        StreamEx.of(list).filter(td -> {
            return TradeTypeEnum.NONE.equalsToCode(td.getTradeType());
        }).forEach(td -> {
            td.setPlate(billIdPlateMap.getOrDefault(td.getBillId(), ""));
        });
        return list;

    }
}