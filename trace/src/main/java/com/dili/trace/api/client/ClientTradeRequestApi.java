package com.dili.trace.api.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dili.assets.sdk.dto.CarTypeDTO;
import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.Attachment;
import com.dili.customer.sdk.domain.VehicleInfo;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.CustomerSimpleExtendDto;
import com.dili.customer.sdk.domain.query.CustomerQueryInput;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.AbstractApi;
import com.dili.trace.api.input.*;
import com.dili.trace.api.output.CheckInApiDetailOutput;
import com.dili.trace.api.output.TradeRequestOutputDto;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.async.AsyncService;
import com.dili.trace.domain.*;
import com.dili.trace.dto.TradeDto;
import com.dili.trace.dto.CustomerExtendOutPutDto;
import com.dili.trace.dto.VehicleInfoDto;
import com.dili.trace.enums.*;
import com.dili.trace.rpc.dto.AccountGetListResultDto;
import com.dili.trace.rpc.dto.CustomerQueryDto;
import com.dili.trace.rpc.service.AttachmentRpcService;
import com.dili.trace.rpc.service.CarTypeRpcService;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.rpc.service.VehicleRpcService;
import com.dili.trace.service.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.swing.text.html.Option;

/**
 * (??????????????????)????????????????????????
 */
@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientTradeRequestApi")
@RestController
@AppAccess(role = Role.Client, url = "", subRoles = {CustomerEnum.CharacterType.?????????, CustomerEnum.CharacterType.??????})
@RequestMapping(value = "/api/client/clientTradeRequestApi")
public class ClientTradeRequestApi extends AbstractApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientTradeRequestApi.class);
    @Autowired
    private LoginSessionContext sessionContext;
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
    TradeOrderService tradeOrderService;

    @Autowired
    ProductStockService productStockService;

    @Autowired
    CustomerRpcService customerRpcService;

    @Autowired
    CarTypeRpcService carTypeRpcService;
    @Autowired
    UserStoreService userStoreService;
    @Autowired
    VehicleRpcService vehicleRpcService;
    @Autowired
    AttachmentRpcService attachmentRpcService;
    @Autowired
    ExtCustomerService extCustomerService;
    @Autowired
    AsyncService asyncService;


    /**
     * ????????????????????????
     *
     * @param condition
     * @return
     */
    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/listPage.api", method = {RequestMethod.POST})
    public BaseOutput<BasePage<TradeRequest>> listPage(@RequestBody TradeRequestListInput condition) {

        try {
            SessionData sessionData = this.sessionContext.getSessionData();
            Long userId = sessionData.getUserId();

            if (condition.getBuyerId() == null && condition.getSellerId() == null) {
                return BaseOutput.failure("????????????");
            }
            if (!userId.equals(condition.getBuyerId())
                    && !userId.equals(condition.getSellerId())) {
                return BaseOutput.failure("????????????");
            }
            if (condition.getBuyerId() != null) {
                condition.setBuyerMarketId(this.sessionContext.getSessionData().getMarketId());
                String orderTypeStrs = StreamEx.of(TradeOrderTypeEnum.values()).map(TradeOrderTypeEnum::getCode).joining(",");
                condition.setMetadata(IDTO.AND_CONDITION_EXPR, "  trade_order_id not  in( select id from trade_order  where  order_type not in(" + orderTypeStrs + ") and `buyer_id` = " + condition.getBuyerId() + " and `buyer_market_id` = " + condition.getBuyerMarketId() + ")");
            }
            if (condition.getSellerId() != null) {
                condition.setSellerMarketId(this.sessionContext.getSessionData().getMarketId());
            }
            condition.setSort("created");
            condition.setOrder("desc");

            BasePage<TradeRequest> page = this.tradeRequestService.listPageByExample(condition);
            List<TradeRequest> data = page.getDatas();
            return BaseOutput.success().setData(page);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }

    }

    /**
     * ??????????????????(??????????????????)
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/viewTradeDetail.api", method = {RequestMethod.POST})
    public BaseOutput<CheckInApiDetailOutput> viewTradeDetail(@RequestBody TradeRequestInputDto inputDto) {

        if (inputDto == null || inputDto.getTradeRequestId() == null) {
            return BaseOutput.failure("????????????");
        }
        try {
            SessionData sessionData = this.sessionContext.getSessionData();

            Long userId = sessionData.getUserId();

            TradeRequest tradeRequestItem = this.tradeRequestService.get(inputDto.getTradeRequestId());
            if (tradeRequestItem == null) {
                return BaseOutput.failure("???????????????");
            }
            if (!userId.equals(tradeRequestItem.getBuyerId()) && !userId.equals(tradeRequestItem.getSellerId())) {
                return BaseOutput.failure("????????????????????????");
            }
            TradeRequestOutputDto out = new TradeRequestOutputDto();
            TradeDetail tradeDetailQuery = new TradeDetail();
            tradeDetailQuery.setTradeRequestId(tradeRequestItem.getId());
            List<TradeDetail> tradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);
            out.setTradeRequest(tradeRequestItem);
            out.setTradeDetailList(tradeDetailList);
            return BaseOutput.success().setData(out);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }

    }

    /**
     * ??????????????????
     *
     * @param inputDto
     * @return
     */
    @ApiOperation(value = "??????????????????")
    @RequestMapping(value = "/createBuyProductRequest.api", method = RequestMethod.POST)
    public BaseOutput<?> createBuyProductRequest(@RequestBody TradeRequestListInput inputDto) {
        if (inputDto == null || inputDto.getTradeMarketId() == null || inputDto.getBatchStockList() == null) {
            return BaseOutput.failure("????????????");
        }
        List<ProductStockInput> batchStockInputList = StreamEx.of(inputDto.getBatchStockList()).nonNull().toList();
        if (batchStockInputList.isEmpty()) {
            return BaseOutput.failure("????????????");
        }
        try {
            SessionData sessionData = this.sessionContext.getSessionData();
            logger.info("buyerId id in session:{}", sessionData.getUserId());

            TradeDto tradeDto = new TradeDto();
            tradeDto.setMarketId(sessionData.getMarketId());
            tradeDto.setTradeOrderType(TradeOrderTypeEnum.SELL);

            tradeDto.getBuyer().setBuyerId(sessionData.getUserId());
            tradeDto.getBuyer().setBuyerName(sessionData.getUserName());
            tradeDto.getBuyer().setBuyerType(BuyerTypeEnum.NORMAL_BUYER);
            TradeOrder tradeOrder = this.tradeOrderService.createBuyTrade(tradeDto, batchStockInputList);
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("??????????????????");
        }

    }

    /**
     * ??????????????????
     *
     * @param inputDto
     * @return
     */
    @ApiOperation(value = "??????????????????")
    @RequestMapping(value = "/createSellProductRequest.api", method = RequestMethod.POST)
    public BaseOutput<?> createSellProductRequest(@RequestBody TradeRequestListInput inputDto) {
        if (inputDto == null || inputDto.getBuyerId() == null || inputDto.getBatchStockList() == null
                || inputDto.getBatchStockList().isEmpty()) {
            return BaseOutput.failure("????????????");
        }

        try {
            SessionData sessionData = this.sessionContext.getSessionData();

            logger.info("seller id in session:{}", sessionData.getUserId());

            TradeDto tradeDto = new TradeDto();
            tradeDto.setMarketId(sessionData.getMarketId());
            tradeDto.setTradeOrderType(TradeOrderTypeEnum.SELL);
            tradeDto.getSeller().setSellerId(sessionData.getUserId());
            tradeDto.getSeller().setSellerName(sessionData.getUserName());

            tradeDto.getBuyer().setBuyerId(inputDto.getBuyerId());

            TradeOrder tradeOrder = this.tradeOrderService.createSellTrade(tradeDto,
                    inputDto.getBatchStockList());
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("??????????????????");
        }

    }

    /**
     * ??????????????????
     *
     * @param inputDto
     * @return
     */
    @ApiOperation(value = "??????????????????")
    @RequestMapping(value = "/createSeperateProductRequest.api", method = RequestMethod.POST)
    public BaseOutput<?> createSeperateProductRequest(@RequestBody TradeRequestListInput inputDto) {
        if (inputDto == null || inputDto.getBuyerId() == null || inputDto.getBatchStockList() == null
                || inputDto.getBatchStockList().isEmpty()) {
            return BaseOutput.failure("????????????");
        }

        try {
            SessionData sessionData = this.sessionContext.getSessionData();

            logger.info("seller id in session:{}", sessionData.getUserId());

            TradeDto tradeDto = new TradeDto();
            tradeDto.setMarketId(sessionData.getMarketId());
            tradeDto.setTradeOrderType(TradeOrderTypeEnum.SEPREATE);
            tradeDto.getSeller().setSellerId(sessionData.getUserId());
            tradeDto.getSeller().setSellerName(sessionData.getUserName());
            tradeDto.getBuyer().setBuyerId(inputDto.getBuyerId());

            TradeOrder tradeOrder = this.tradeOrderService.createSeperateTrade(tradeDto,
                    inputDto.getBatchStockList());
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("??????????????????");
        }

    }

    /**
     * ????????????ID????????????????????????
     *
     * @param inputDto
     * @return
     */
    @ApiOperation(value = "????????????ID????????????????????????")
    @RequestMapping(value = "/listPageTradeRequestByTraderOrderId.api", method = RequestMethod.POST)
    public BaseOutput<?> listPageTradeRequestByTraderOrderId(@RequestBody TradeRequestInputDto inputDto) {
        if (inputDto == null || inputDto.getTraderOrderId() == null) {
            return BaseOutput.failure("????????????");
        }
        try {
            SessionData sessionData = this.sessionContext.getSessionData();

            Long sellerId = sessionData.getUserId();

            TradeRequest request = new TradeRequest();
            request.setTradeOrderId(inputDto.getTraderOrderId());
            return BaseOutput.success().setData(this.tradeRequestService.listPageByExample(request));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("??????????????????");
        }

    }

    /**
     * ????????????
     *
     * @param inputDto
     * @return ??????????????????
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/createReturning.api", method = {RequestMethod.POST})
    public BaseOutput<Long> createReturning(@RequestBody TradeRequestInputDto inputDto) {
        try {
            SessionData sessionData = this.sessionContext.getSessionData();

            Long userId = sessionData.getUserId();

            Long id = this.tradeRequestService.createReturning(inputDto.getTradeRequestId(), userId);
            return BaseOutput.success().setData(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }

    }

    /**
     * ????????????
     *
     * @param inputDto
     * @return ??????????????????
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/handleReturning.api", method = {RequestMethod.POST})
    public BaseOutput<Long> handleReturning(@RequestBody TradeRequestInputDto inputDto) {
        try {
            TradeReturnStatusEnum returnStatus = TradeReturnStatusEnum.fromCode(inputDto.getReturnStatus())
                    .orElse(null);
            if (returnStatus == null || inputDto.getTradeRequestId() == null) {
                return BaseOutput.failure("????????????");
            }
            SessionData sessionData = this.sessionContext.getSessionData();

            Long userId = sessionData.getUserId();
            Long id = this.tradeRequestService.handleReturning(inputDto.getTradeRequestId(), userId, returnStatus,
                    inputDto.getReason());
            return BaseOutput.success().setData(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param inputDto
     * @return
     */
    @RequestMapping(value = "/handleBuyerRquest.api", method = {RequestMethod.POST})
    public BaseOutput handleBuyRequest(@RequestBody TradeRequestHandleDto inputDto) {
        try {
            this.tradeOrderService.handleBuyerRequest(inputDto);
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }
    }

    /**
     * ????????????????????????
     *
     * @param buyerId ??????ID
     * @return ????????????
     */
    @RequestMapping(value = "/listBuyHistory.api", method = {RequestMethod.GET})
    public BaseOutput<List<UserOutput>> listBuyHistory(@RequestParam Long buyerId) {
        try {
            List<UserOutput> list = this.tradeRequestService.queryTradeSellerHistoryList(buyerId);
            return BaseOutput.success().setData(list);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param query
     * @return ????????????
     */
    @RequestMapping(value = "/listSeller.api", method = {RequestMethod.POST})
    public BaseOutput listSeller(@RequestBody CustomerQueryDto query) {
        try {
            Long marketId = this.sessionContext.getSessionData().getMarketId();
            query.setPage(1);
            //???100???????????????????????????
            query.setRows(50);
            query.setMarketId(marketId);
            PageOutput<List<CustomerSimpleExtendDto>> pageOutput = this.customerRpcService.listSeller(query);

            // UAP ??????????????????????????????????????????????????????????????????????????????
            List<CustomerExtendOutPutDto> list = getListPageOutput(marketId, pageOutput, ClientTypeEnum.SELLER).getData();

            return BaseOutput.successData(list);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("??????????????????????????????");
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param marketId
     * @param pageOutput
     * @return
     */
    private PageOutput<List<CustomerExtendOutPutDto>> getListPageOutput(Long marketId, PageOutput<List<CustomerSimpleExtendDto>> pageOutput, ClientTypeEnum clientTypeEnum) {
        PageOutput<List<CustomerExtendOutPutDto>> page = new PageOutput<>();
        if (null != pageOutput) {

            List<CustomerSimpleExtendDto> customerList = pageOutput.getData();
            List<Long> customerIdList = StreamEx.ofNullable(customerList).flatCollection(Function.identity()).nonNull().map(CustomerSimpleExtendDto::getId).toList();


            Map<Long, List<VehicleInfoDto>> vehicleInfoMap = Maps.newHashMap();
            Map<Long, Attachment> attachmentMap = Maps.newHashMap();
            Map<Long, AccountGetListResultDto> cardNoTraceCustomerMap = Maps.newHashMap();

            CompletableFuture<?> vehicleInfoMapFuture = this.asyncService.execAsync(() -> {
                Map<Long, List<VehicleInfoDto>>retMap= this.vehicleRpcService.findVehicleInfoByMarketIdAndCustomerIdList(marketId, customerIdList);
                vehicleInfoMap.putAll(retMap);
                return null;
            });
            CompletableFuture<?> attachmentMapFuture = this.asyncService.execAsync(() -> {
                Map<Long, Attachment>retMap=  this.attachmentRpcService.findAttachmentByAttachmentTypeAndCustomerIdList(marketId, customerIdList, CustomerEnum.AttachmentType.????????????);
                attachmentMap.putAll(retMap);
                return null;
            });
            CompletableFuture<?> cardNoTraceCustomerMapFuture = this.asyncService.execAsync(() -> {
                Map<Long, AccountGetListResultDto>retMap=  this.extCustomerService.findCardInfoByCustomerIdList(marketId, customerIdList);
                cardNoTraceCustomerMap.putAll(retMap);
                return null;
            });
            try {
                CompletableFuture.allOf(vehicleInfoMapFuture,attachmentMapFuture,cardNoTraceCustomerMapFuture).join();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }


            List<CustomerExtendOutPutDto> customerOutputList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(customerList)) {
                customerList.forEach(c -> {
                    CustomerExtendOutPutDto customerOutput = new CustomerExtendOutPutDto();
                    customerOutput.setMarketId(marketId);
                    customerOutput.setMarketName(this.sessionContext.getSessionData().getMarketName());
                    customerOutput.setId(c.getId());
                    customerOutput.setName(c.getName());
                    customerOutput.setOrganizationType(c.getOrganizationType());
                    customerOutput.setBusinessLicenseAttachment(attachmentMap.get(c.getId()));
                    customerOutput.setPhone(c.getContactsPhone());
                    customerOutput.setClientType(clientTypeEnum.getCode());
                    customerOutput.setCardNo(cardNoTraceCustomerMap.getOrDefault(c.getId(), new AccountGetListResultDto()).getCardNo());
                    List<VehicleInfoDto> vehicleInfoDtoList = vehicleInfoMap.getOrDefault(c.getId(), Lists.newArrayList());
                    customerOutput.setVehicleInfoList(vehicleInfoDtoList);

                    customerOutputList.add(customerOutput);
                });
            }
            BeanUtils.copyProperties(pageOutput, page);
            page.setData(customerOutputList);
        }
        return page;
    }

    /**
     * ???????????????????????????
     *
     * @param userId ??????ID
     * @return ????????????
     */
    @RequestMapping(value = "/listSaleableProduct.api", method = {RequestMethod.GET})
    public BaseOutput<List<ProductStock>> listSaleableProduct(@RequestParam Long userId) {
        try {
            ProductStock productStock = new ProductStock();
            productStock.setUserId(userId);
            productStock.setMetadata(IDTO.AND_CONDITION_EXPR, "stock_weight > 0");
            List<ProductStock> list = this.productStockService.listByExample(productStock);
            return BaseOutput.success().setData(list);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }
    }


}