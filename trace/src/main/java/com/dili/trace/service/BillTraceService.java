package com.dili.trace.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.trace.api.output.TraceDataDto;
import com.dili.trace.api.output.TraceDetailOutputDto;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.rpc.dto.CustDto;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.rpc.service.FirmRpcService;
import com.dili.uap.sdk.domain.Firm;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;

/**
 * SB
 */
@Service
public class BillTraceService {
    //    @Autowired
//    private UserService userService;
    @Autowired
    CustomerRpcService customerRpcService;
    @Autowired
    FirmRpcService firmRpcService;
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
    ProductStockService batchStockService;
    @Autowired
    ImageCertService imageCertService;

    /**
     * SB
     *
     * @param tradeRequestid
     * @param userId
     * @return
     */
    public TraceDetailOutputDto viewBillTrace(Long tradeRequestid, Long userId) {
        TradeRequest tradeRequestItem = this.tradeRequestService.get(tradeRequestid);
        if (tradeRequestItem == null) {
            throw new TraceBizException("没有查找到详情");
        }

        TraceDetailOutputDto traceDetailOutputDto = new TraceDetailOutputDto();
        traceDetailOutputDto.setTradeRequestId(tradeRequestItem.getTradeRequestId());
        traceDetailOutputDto.setCreated(tradeRequestItem.getCreated());
        if (tradeRequestItem.getBuyerId()!=null&&tradeRequestItem.getBuyerId().equals(userId)) {

            CustomerExtendDto seller = this.customerRpcService.findCustomerByIdOrEx(tradeRequestItem.getSellerId(), tradeRequestItem.getSellerMarketId());
            String sellerMarketName = this.firmRpcService.getFirmByIdOrEx(tradeRequestItem.getSellerMarketId()).getName();
            String sellerTallyAreaNos = StreamEx.ofNullable(seller).map(CustomerExtendDto::getTallyingAreaList).nonNull()
                    .flatCollection(Function.identity()).nonNull()
                    .map(TallyingArea::getAssetsName).distinct().joining(",");


            TraceDataDto upTrace = new TraceDataDto();
            upTrace.setCreated(tradeRequestItem.getCreated());
            upTrace.setBuyerName(tradeRequestItem.getBuyerName());
            upTrace.setSellerName(tradeRequestItem.getSellerName());
            upTrace.setMarketName(sellerMarketName);
            upTrace.setTallyAreaNo(sellerTallyAreaNos);

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
                        CustDto cust=this.findCustByIdAndMarketId(tr.getBuyerId(), tr.getBuyerMarketId());
                        TraceDataDto downTrace = new TraceDataDto();
                        downTrace.setCreated(tr.getCreated());
                        downTrace.setBuyerName(tr.getBuyerName());
                        downTrace.setSellerName(tr.getSellerName());
                        downTrace.setMarketName(cust.getMarketName());
                        downTrace.setTallyAreaNo(cust.getTallyAreaNos());
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
                        CustDto cust=this.findCustByIdAndMarketId(tr.getBuyerId(), tr.getBuyerMarketId());

                        TraceDataDto upTraceDto = new TraceDataDto();
                        upTraceDto.setCreated(tr.getCreated());
                        upTraceDto.setBuyerName(tr.getBuyerName());
                        upTraceDto.setSellerName(tr.getSellerName());
                        upTraceDto.setMarketName(cust.getMarketName());
                        upTraceDto.setTallyAreaNo(cust.getTallyAreaNos());
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
                        if (requestId == null) {
                            return null;
                        }
                        TradeRequest tr = this.tradeRequestService.get(requestId);
                        if (tr == null) {
                            return null;
                        }
                        try {
                            CustDto cust=this.findCustByIdAndMarketId(tr.getBuyerId(), tr.getBuyerMarketId());
                            TraceDataDto downTrace = new TraceDataDto();
                            downTrace.setCreated(tr.getCreated());
                            downTrace.setBuyerName(tr.getBuyerName());
                            downTrace.setSellerName(tr.getSellerName());
                            downTrace.setMarketName(cust.getMarketName());
                            downTrace.setTallyAreaNo(cust.getTallyAreaNos());
                            return downTrace;
                        }catch (TraceBizException e){
                            return null;
                        }
                    }).nonNull().toList();
            traceDetailOutputDto.setUpTraceList(upTraceList);
            traceDetailOutputDto.setDownTraceList(downTraceList);
        } else {
            throw new TraceBizException("没有查询到数据");
        }
        ProductStock batchStockItem = this.batchStockService.get(tradeRequestItem.getProductStockId());
        traceDetailOutputDto.setBrandName(batchStockItem.getBrandName());
        traceDetailOutputDto.setSpecName(batchStockItem.getSpecName());
        traceDetailOutputDto.setProductName(batchStockItem.getProductName());
        TradeDetail query = new TradeDetail();
        query.setTradeRequestId(tradeRequestItem.getId());
        List<TradeDetail> traceList = this.tradeDetailService.listByExample(query);
        List<Long> billIdList = StreamEx.of(traceList).map(TradeDetail::getBillId).distinct().toList();
        List<ImageCert> imageCertList = this.imageCertService.findImageCertListByBillIdList(billIdList, BillTypeEnum.REGISTER_BILL);
        traceDetailOutputDto.setImageCertList(imageCertList);

        return traceDetailOutputDto;

    }

    /**
     * SB
     *
     * @param tradeRequestId
     * @param userId
     * @return
     */
    public List<TradeDetail> viewTradeDetailList(Long tradeRequestId, Long userId) {

        TradeRequest tradeRequestItem = this.tradeRequestService.get(tradeRequestId);
        if (tradeRequestItem == null) {
            throw new TraceBizException("没有查找到详情");
        }
        List<TradeDetail> list = Lists.newArrayList();
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
            list.addAll(buyerTradeDetailList);
            ;

        } else {
            throw new TraceBizException("没有查询到数据");
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

    /**
     * 查询客户相关信息
     * @param userId
     * @param marketId
     * @return
     */
    private CustDto findCustByIdAndMarketId(Long userId, Long marketId) {

        CustomerExtendDto buyer = this.customerRpcService.findCustomerByIdOrEx(userId, marketId);
        String buyerMarketName = this.firmRpcService.getFirmByIdOrEx(marketId).getName();
        String buyerTallyAreaNos = StreamEx.ofNullable(buyer).map(CustomerExtendDto::getTallyingAreaList).nonNull()
                .flatCollection(Function.identity()).nonNull()
                .map(TallyingArea::getAssetsName).distinct().joining(",");
        CustDto cust=new CustDto();
        cust.setUserId(userId);
        cust.setUserName(buyer.getName());
        cust.setMarketId(marketId);
        cust.setMarketName(buyerMarketName);
        cust.setTallyAreaNos(buyerTallyAreaNos);
        return cust;


    }
}