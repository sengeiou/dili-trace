package com.dili.trace.service;

import java.util.List;

import javax.annotation.Resource;

import com.dili.common.exception.TraceBusinessException;
import com.dili.trace.api.output.TraceDataDto;
import com.dili.trace.api.output.TraceDetailOutputDto;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
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
            // BatchStock
            // batchStock=this.batchStockService.get(tradeRequestItem.getBatchStockId());
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
                    .distinct().map(requestId -> {

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
                    .map(TradeDetail::getTradeRequestId).distinct().map(requestId -> {

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

            User seller = this.userService.get(tradeRequestItem.getSellerId());
            // BatchStock
            // batchStock=this.batchStockService.get(tradeRequestItem.getBatchStockId());
            TraceDataDto upTrace = new TraceDataDto();
            upTrace.setCreated(tradeRequestItem.getCreated());
            upTrace.setBuyerName(tradeRequestItem.getBuyerName());
            upTrace.setSellerName(tradeRequestItem.getSellerName());
            upTrace.setMarketName(seller.getMarketName());
            upTrace.setTallyAreaNo(seller.getTallyAreaNos());

            TradeDetail tradeDetailQuery = new TradeDetail();
            tradeDetailQuery.setSellerId(userId);
            tradeDetailQuery.setTradeRequestId(tradeRequestItem.getId());
            List<Long> parentIdList = StreamEx.of(this.tradeDetailService.listByExample(tradeDetailQuery))
                    .map(TradeDetail::getId).toList();
            List<TradeDetail> buyerTradeDetailList = this.tradeDetailService
                    .findTradeDetailByParentIdList(parentIdList);

            List<TraceDataDto> downTraceList = StreamEx.of(buyerTradeDetailList).map(TradeDetail::getTradeRequestId)
                    .distinct().map(requestId -> {

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
            traceDetailOutputDto.setUpTraceList(upTraceList);
            traceDetailOutputDto.setDownTraceList(downTraceList);
        } else {
            throw new TraceBusinessException("没有查询到数据");
        }
        BatchStock batchStockItem = this.batchStockService.get(tradeRequestItem.getBatchStockId());
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
}