package com.dili.trace.rpc.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.rpc.api.LeaseOrderRpc;
import com.dili.trace.rpc.dto.AssetsParamsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 调用rpc，查询可用摊位号
 */
@Service
public class LeaseOrderRpcService {
    @Autowired
    LeaseOrderRpc leaseOrderRpc;

    /**
     * 调用rpc，查询可用摊位号
     *
     * @param assetsParamsDto
     * @return
     */
    public BaseOutput<Object> findLease(AssetsParamsDto assetsParamsDto) {

        return this.leaseOrderRpc.findLease(assetsParamsDto);
    }
}
