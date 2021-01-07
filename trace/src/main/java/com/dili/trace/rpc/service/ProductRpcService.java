package com.dili.trace.rpc.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.StockInUnitEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.glossary.StockRegisterSourceEnum;
import com.dili.trace.rpc.api.ProductRpc;
import com.dili.trace.rpc.dto.*;
import com.dili.trace.service.BillService;
import com.dili.trace.service.TradeDetailService;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 库存服务
 */
@Service
public class ProductRpcService {

    private static final Logger logger = LoggerFactory.getLogger(ProductRpcService.class);

    @Autowired(required = false)
    ProductRpc productRpc;
    @Autowired
    FirmRpcService firmRpcService;
    @Autowired
    BillService billService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    UidRestfulRpcService uidRestfulRpcService;

    /**
     * 创建库存
     *
     * @param bill
     * @param opt
     */
    @Transactional(rollbackFor = Exception.class)
    public RegCreateResultDto create(RegisterBill bill, Optional<OperatorUser> opt) {

        try {
            // 库存基本信息
            RegCreateDto createDto = this.buildCreateDtoFromBill(bill, opt, bill.getMarketId());
            // 远程调用库存接口
            BaseOutput<RegCreateResultDto> out = this.productRpc.create(createDto);

            if (out.isSuccess() && out.getData() != null) {
                // 同步完成后更新和溯源库存的关联关系
                updateStockIdAfterCreate(out, bill.getId());
                logger.debug("创建库存成功");
                return out.getData();
            } else {
                logger.error("创建库存失败:{}",out.getMessage());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
//        throw new TraceBizException("创建库存失败");
    }

    /**
     * 扣减库存
     *
     * @param bill
     * @param opt
     */
    public List<StockReductResultDto> reduceByStockIds(Long stockId, RegisterBill bill, Optional<OperatorUser> opt) {
        try {
            //库存基本信息
            StockReduceRequestDto obj = new StockReduceRequestDto();
            obj.setFirmId(bill.getMarketId());
            this.firmRpcService.getFirmById(bill.getMarketId()).ifPresent(firm -> {
                obj.setFirmName(firm.getName());
            });

            StockReduceDto stockReduceDto = new StockReduceDto();
            // 库存id
            stockReduceDto.setBizId(bill.getId());
            stockReduceDto.setReduceNum(bill.getWeight());
            // 业务单据号
            stockReduceDto.setStockId(stockId);
            obj.setStockReduceItems(Lists.newArrayList(stockReduceDto));

            BaseOutput<List<StockReductResultDto>> out = this.productRpc.reduceByStockIds(obj);
            if (out.isSuccess() && out.getData() != null) {
                logger.debug("扣减库存成功");
                return out.getData();
            } else {
                logger.error("扣减库存失败:{}",out.getMessage());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return Lists.newArrayList();
       // throw new TraceBizException("扣减库存失败");
    }

    /**
     * 处理交易单库存
     * @param buyerDetailList
     * @param sellerDetailList
     * @param optUser
     * @param marketId
     */
    public void handleTradeStocks(List<TradeDetail> buyerDetailList, List<TradeDetail> sellerDetailList,
                                  Optional<OperatorUser> optUser, Long marketId) {

        // 构建创建库存基础信息
        List<RegCreateDto> createDtos = buildCreateDtoFromTrade(buyerDetailList, optUser, marketId);

        // 远程调用库存接口
        StreamEx.of(createDtos).forEach(createDto -> {
            BaseOutput<RegCreateResultDto> out = this.productRpc.create(createDto);
            if (out.isSuccess() && out.getData() != null) {
                List<RegDetailDto> regDetailDtos = out.getData().getRegDetailDtos();
                if (!CollectionUtils.isEmpty(regDetailDtos)) {
                    // 同步完成后更新和溯源库存的关联关系
                    TradeDetail condition = new TradeDetail();
                    condition.setId(createDto.getTradeDetailId());
                    condition.setThirdPartyStockId(regDetailDtos.get(0).getStockId());
                    tradeDetailService.updateSelective(condition);
                    logger.debug("创建库存成功");
                } else {
                    logger.error("创建库存成功，但是未返回StockId");
                }
            } else {
                logger.error("创建库存失败：{}",out.getMessage());
                //throw new TraceBizException("创建库存失败");
            }
        });

        // 构建扣减库存基础信息
        StockReduceRequestDto obj = buildReduceDtoFromTrade(sellerDetailList, optUser, marketId);
        BaseOutput<List<StockReductResultDto>> out = this.productRpc.reduceByStockIds(obj);
        if (out.isSuccess() && out.getData() != null) {
            return;
        }
        logger.error("创建库存失败：{}",out.getMessage());
        return;
       // throw new TraceBizException("扣减库存失败");
    }

    /**
     *
     * @param detailList
     * @param optUser
     * @param marketId
     * @return
     */
    private StockReduceRequestDto buildReduceDtoFromTrade(List<TradeDetail> detailList, Optional<OperatorUser> optUser, Long marketId) {

        if (CollectionUtils.isEmpty(detailList)) {
            throw new TraceBizException("处理库存失败，因为交易批次为空");
        }

        StockReduceRequestDto obj = new StockReduceRequestDto();
        obj.setFirmId(marketId);
        this.firmRpcService.getFirmById(marketId).ifPresent(firm -> {
            obj.setFirmName(firm.getName());
        });

        List<StockReduceDto> reduceDtoList = new ArrayList<>();
        StreamEx.of(detailList).forEach(d -> {
            StockReduceDto stockReduceDto = new StockReduceDto();
            // 业务单据号
            stockReduceDto.setBizId(d.getBillId());
            stockReduceDto.setReduceNum(d.getStockWeight());
            // 库存ID
            stockReduceDto.setStockId(d.getThirdPartyStockId());
            reduceDtoList.add(stockReduceDto);
        });

        obj.setStockReduceItems(reduceDtoList);

        return obj;
    }

    /**
     * 同步完成 UAP 库存后，溯源本地维护 stockId
     * @param out
     * @param billId
     */
    private void updateStockIdAfterCreate(BaseOutput<RegCreateResultDto> out, Long billId) {
        List<RegDetailDto> regDetailDtos = out.getData().getRegDetailDtos();
        Optional<TradeDetail> tradeDetail = tradeDetailService.findBilledTradeDetailByBillId(billId);
        tradeDetail.ifPresent(t -> {
            TradeDetail condition = new TradeDetail();
            condition.setId(tradeDetail.get().getId());
            condition.setThirdPartyStockId(regDetailDtos.get(0).getStockId());
            tradeDetailService.updateSelective(condition);
        });
    }

    /**
     * 根据交易批次构建库存创建 DTO
     * @param detailList
     * @param optUser
     * @param marketId
     * @return
     */
    private List<RegCreateDto> buildCreateDtoFromTrade(List<TradeDetail> detailList, Optional<OperatorUser> optUser, Long marketId) {

        if (CollectionUtils.isEmpty(detailList)) {
            throw new TraceBizException("处理库存失败，因为交易批次为空");
        }

        List<RegCreateDto> createDtoList = new ArrayList<>();
        //库存基本信息
        StreamEx.of(detailList).forEach(d -> {
            RegisterBill bill = billService.getAvaiableBill(d.getBillId()).orElseThrow(() -> {
                return new TraceBizException("处理库存失败，因为报备单" + d.getBillId() + "查询失败");
            });
            bill.setId(d.getBillId());
            bill.setUserId(d.getBuyerId());
            bill.setName(d.getBuyerName());
            bill.setWeight(d.getStockWeight());
            bill.setWeightUnit(d.getWeightUnit());
            RegCreateDto createDto = this.buildCreateDtoFromBill(bill, optUser, marketId);
            // 设置溯源批次和UAP批次映射
            createDto.setTradeDetailId(d.getId());
            createDtoList.add(createDto);
        });

        return createDtoList;
    }

    /**
     * 根据报备单构建库存创建 DTO
     * @param bill
     * @param optUser
     * @param marketId
     * @return
     */
    private RegCreateDto buildCreateDtoFromBill(RegisterBill bill, Optional<OperatorUser> optUser, Long marketId) {
        RegCreateDto obj = new RegCreateDto();
        obj.setFirmId(marketId);
        this.firmRpcService.getFirmById(marketId).ifPresent(firm -> {
            obj.setFirmName(firm.getName());
        });
        // 库存系统要校验InStockNo字段唯一，传报备单主键交易场景有问题，所以生成个唯一单号
        obj.setInStockNo(uidRestfulRpcService.bizNumber(BizNumberType.STOCK_CODE.getType()));
        optUser.ifPresent(o -> {
            obj.setOperatorId(o.getId());
            obj.setOperatorName(o.getName());
        });
        obj.setPlateNo(bill.getPlate());

        //库存详情信息
        RegDetailDto detailDto = new RegDetailDto();
        detailDto.setBrand(bill.getBrandName());
        detailDto.setSpec(bill.getSpecName());
        // 买家增加库存
        detailDto.setCustomerId(bill.getUserId());
        detailDto.setCustomerName(bill.getName());
        detailDto.setCateId(bill.getProductId());
        detailDto.setCname(bill.getProductName());

        if (WeightUnitEnum.JIN.equalsToCode(bill.getWeightUnit())) {
            detailDto.setInUnit(StockInUnitEnum.JIN.getCode());
        } else if (WeightUnitEnum.KILO.equalsToCode(bill.getWeightUnit())) {
            detailDto.setInUnit(StockInUnitEnum.KILO.getCode());
        } else {
            detailDto.setInUnit(StockInUnitEnum.PIECE.getCode());
        }
        detailDto.setPlace(bill.getOriginName());
        detailDto.setPrice(bill.getUnitPrice());
        detailDto.setWeight(bill.getWeight());
        detailDto.setProductId(bill.getProductId());
        detailDto.setProductName(bill.getProductName());

        obj.setRegDetailDtos(Lists.newArrayList(detailDto));
        obj.setSource(StockRegisterSourceEnum.REG.getCode());
        // obj.setInStockNo(bill.getCode());
        return obj;
    }
}
