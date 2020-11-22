package com.dili.trace.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.rpc.FirmRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 对rpc进行封装
 */
@Service
public class FirmRpcService {
    @Autowired(required = false)
    FirmRpc firmRpc;

    /**
     * 查询当前市场
     * @param firmId
     * @return
     */
    public Optional<Firm> getFirmById(Long firmId) {
        if (firmId == null) {
            return Optional.empty();
        }
        BaseOutput<Firm>out=this.firmRpc.getById(firmId);
        if(out==null||!out.isSuccess()){
            return Optional.empty();
        }
        return Optional.ofNullable(out.getData());
    }

}
