package com.dili.trace.rpc.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.glossary.StockRegisterSourceEnum;
import com.dili.trace.rpc.api.ProductRpc;
import com.dili.trace.rpc.dto.*;
import com.dili.trace.service.BillService;
import com.dili.trace.service.TradeDetailService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 库存服务
 */
@Service
public class ProductRpcService {
    @Autowired(required = false)
    ProductRpc productRpc;
    @Autowired
    FirmRpcService firmRpcService;
    @Autowired
    BillService billService;
    @Autowired
    TradeDetailService tradeDetailService;

    /**
     * 创建库存
     *
     * @param bill
     * @param opt
     */
    @Transactional(rollbackFor = Exception.class)
    public RegCreateResultDto create(RegisterBill bill, Optional<OperatorUser> opt) {
        //库存基本信息
        RegCreateDto obj = new RegCreateDto();
        obj.setFirmId(bill.getMarketId());
        obj.setFirmName(this.firmRpcService.getFirmById(bill.getMarketId()).get().getName());
        obj.setInStockNo(String.valueOf(bill.getBillId()));

        opt.ifPresent(o -> {
            obj.setOperatorId(o.getId());
            obj.setOperatorName(o.getName());
        });
        obj.setPlateNo(bill.getPlate());

        //库存详情信息
        RegDetailDto detailDto = new RegDetailDto();

        detailDto.setBrand(bill.getBrandName());
        detailDto.setSpec(bill.getSpecName());
        detailDto.setCustomerId(bill.getUserId());
        detailDto.setCustomerName(bill.getName());
        detailDto.setCateId(bill.getProductId());
        detailDto.setCname(bill.getProductName());
        if (WeightUnitEnum.JIN.equalsToCode(bill.getWeightUnit())) {
            detailDto.setInUnit(1);
        } else if (WeightUnitEnum.KILO.equalsToCode(bill.getWeightUnit())) {
            detailDto.setInUnit(2);
        } else {
            detailDto.setInUnit(3);
        }
        detailDto.setPlace(bill.getOriginName());
        detailDto.setPrice(bill.getUnitPrice());
        detailDto.setWeight(bill.getWeight());

        obj.setRegDetailDtos(Lists.newArrayList(detailDto));
        obj.setSource(StockRegisterSourceEnum.REG.getCode());

        BaseOutput<RegCreateResultDto> out = this.productRpc.create(obj);
        if (out.isSuccess() && out.getData() != null) {
            // 同步完成后更新和溯源库存的关联关系
            List<RegDetailDto> regDetailDtos = out.getData().getRegDetailDtos();
            Optional<TradeDetail> tradeDetail = tradeDetailService.findBilledTradeDetailByBillId(bill.getId());
            tradeDetail.ifPresent(t -> {
                TradeDetail condition = new TradeDetail();
                condition.setId(tradeDetail.get().getId());
                condition.setThirdPartyStockId(regDetailDtos.get(0).getStockId());
                tradeDetailService.updateSelective(condition);
            });
            return out.getData();
        }
        throw new TraceBizException("创建库存失败");
    }

    /**
     * 扣减库存
     *
     * @param bill
     * @param opt
     */
    public List<StockReductResultDto> reduceByStockIds(Long stockId, RegisterBill bill, Optional<OperatorUser> opt) {
        //库存基本信息
        StockReduceRequestDto obj = new StockReduceRequestDto();
        obj.setFirmId(bill.getMarketId());
        obj.setFirmName(this.firmRpcService.getFirmById(bill.getMarketId()).get().getName());

        StockReduceDto stockReduceDto = new StockReduceDto();
        // 库存id
        stockReduceDto.setBizId(bill.getId());
        stockReduceDto.setReduceNum(bill.getWeight());
        // 业务单据号
        stockReduceDto.setStockId(stockId);
        obj.setStockReduceItems(Lists.newArrayList(stockReduceDto));

        BaseOutput<List<StockReductResultDto>> out = this.productRpc.reduceByStockIds(obj);
        if (out.isSuccess() && out.getData() != null) {
            return out.getData();
        }
        throw new TraceBizException("扣减库存失败");
    }

}
