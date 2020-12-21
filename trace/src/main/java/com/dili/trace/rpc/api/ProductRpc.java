package com.dili.trace.rpc.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.trace.rpc.dto.RegCreateDto;
import com.dili.trace.rpc.dto.RegCreateResultDto;
import com.dili.trace.rpc.dto.StockReduceRequestDto;
import com.dili.trace.rpc.dto.StockReductResultDto;

import java.util.List;

/**
 * 库存系统 rpc
 *
 * @author wangguofeng
 */
@Restful("${product-service.contextPath}")
public interface ProductRpc {


    /**
     * 库存扣减
     *
     * @param obj
     * @return
     */
    @POST("/api/stock/reduceByStockIds")
    public BaseOutput<StockReductResultDto> reduceByStockIds(@VOBody StockReduceRequestDto obj);

    /**
     * 创建批次库存
     * @param obj
     * @return
     */
    @POST("/api/register/create")
    public BaseOutput<RegCreateResultDto> create(@VOBody RegCreateDto obj);
}