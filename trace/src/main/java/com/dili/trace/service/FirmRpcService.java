package com.dili.trace.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.sdk.rpc.FirmRpc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * 根据市场编码查询指定用户市场
     * @param firmCode
     * @return
     */
    public Optional<Firm> getFirmByCode(String firmCode) {
        if (StringUtils.isBlank(firmCode)) {
            return Optional.empty();
        }
        BaseOutput<Firm>out=this.firmRpc.getByCode(firmCode);
        if(out==null||!out.isSuccess()){
            return Optional.empty();
        }
        return Optional.ofNullable(out.getData());
    }

    /**
     * 查询市场列表
     * @param firmDto
     * @return
     */
    public Optional<List<Firm>> getFirms(FirmDto firmDto) {
        if (firmDto == null) {
            return Optional.empty();
        }
        BaseOutput<List<Firm>> out = firmRpc.listByExample(firmDto);
        if(out==null||!out.isSuccess()){
            return Optional.empty();
        }
        return Optional.ofNullable(out.getData());
    }

}
