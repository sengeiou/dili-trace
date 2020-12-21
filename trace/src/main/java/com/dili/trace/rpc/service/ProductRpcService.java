package com.dili.trace.rpc.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.rpc.api.ProductRpc;
import com.dili.trace.rpc.dto.RegCreateDto;
import com.dili.trace.rpc.dto.RegCreateResultDto;
import com.dili.trace.rpc.dto.RegDetailDto;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    /**
     * 创建库存
     *
     * @param bill
     * @param opt
     */
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

        BaseOutput<RegCreateResultDto> out = this.productRpc.create(obj);
        if (out.isSuccess() && out.getData() != null) {
            return out.getData();
        }
        throw new TraceBizException("创建库存失败");

    }
}
