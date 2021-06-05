package com.dili.trace.rpc.service;

import com.dili.assets.sdk.dto.CarTypeDTO;
import com.dili.customer.sdk.domain.VehicleInfo;
import com.dili.customer.sdk.rpc.VehicleRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.VehicleInfoDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 车牌信息
 */
@Service
public class VehicleRpcService {
    private static final Logger logger = LoggerFactory.getLogger(VehicleRpcService.class);
    @Autowired
    VehicleRpc vehicleRpc;
    @Autowired
    CarTypeRpcService carTypeRpcService;

    /**
     * 根据市场id和customerid查询
     *
     * @param marketId
     * @param customerId
     * @return
     */
    public List<VehicleInfo> findVehicleInfoByMarketIdAndCustomerId(Long marketId, Long customerId) {
        if (marketId == null || customerId == null) {
            return Lists.newArrayList();
        }

        try {
            BaseOutput<List<VehicleInfo>> out = this.vehicleRpc.listVehicle(customerId, marketId);
            if (out == null) {
                logger.error("查询返回BaseOutput为Null");
                return Lists.newArrayList();
            }
            if (!out.isSuccess()) {
                logger.error("查询返回Message为:{}", out.getMessage());
                return Lists.newArrayList();
            }
            return out.getData();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Lists.newArrayList();
        }
    }


    /**
     * 根据市场id和多个customerid查询
     *
     * @param marketId
     * @param customerIdList
     * @return
     */
    public Map<Long, List<VehicleInfoDto>> findVehicleInfoByMarketIdAndCustomerIdList(Long marketId, List<Long> customerIdList) {
        if (marketId == null || customerIdList == null || customerIdList.isEmpty()) {
            return Maps.newHashMap();
        }

        try {
            BaseOutput<Map<Long, List<VehicleInfo>>> out = this.vehicleRpc.batchQuery(Sets.newHashSet(customerIdList), marketId);
            if (out == null) {
                logger.error("查询返回BaseOutput为Null");
                return Maps.newHashMap();
            }
            if (!out.isSuccess()) {
                logger.error("查询返回Message为:{}", out.getMessage());
                return Maps.newHashMap();
            }

            Map<Long, String> carTypeMap = StreamEx.ofNullable(this.carTypeRpcService.listCarType()).flatCollection(Function.identity())
                    .mapToEntry(CarTypeDTO::getId, CarTypeDTO::getName).toMap();

            Map<Long, List<VehicleInfo>> map = out.getData();
            return EntryStream.of(map == null ? Maps.newHashMap() : map).mapValues(list -> {
                return StreamEx.ofNullable(list).flatCollection(Function.identity()).nonNull().map(v -> {
                    VehicleInfoDto dto = VehicleInfoDto.build(v, null);
                    String vehicleTypeName = carTypeMap.getOrDefault(dto.getVehicleTypeNumber(), "");
                    dto.setVehicleTypeName(vehicleTypeName);
                    return dto;
                }).toList();

            }).toMap();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Maps.newHashMap();
        }

    }
}
