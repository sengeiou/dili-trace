package com.dili.trace.rpc.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.trace.rpc.dto.RegCreateDto;
import com.dili.trace.rpc.dto.RegCreateResultDto;
import com.dili.trace.rpc.dto.StockReduceRequestDto;
import com.dili.trace.rpc.dto.StockReductResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 库存系统 rpc
 *
 * @author wangguofeng
 */
@FeignClient(
        name = "product-service",
        contextId = "productRpc",
        url = "${productService.url:}"
)
public interface ProductRpc {


    /**
     * 库存扣减
     *
     * @param obj
     * @return
     */
    @PostMapping(value="/api/stock/reduceByStockIds",consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseOutput<StockReductResultDto> reduceByStockIds(@RequestBody StockReduceRequestDto obj);

    /**
     * 创建批次库存
     * @param obj
     * @return
     */
    @PostMapping(value="/api/register/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseOutput<RegCreateResultDto> create(@VOBody RegCreateDto obj);
}