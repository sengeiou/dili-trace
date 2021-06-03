package com.dili.trace.rpc.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.util.TraceUtil;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.sdk.rpc.FirmRpc;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 对rpc进行封装
 */
@Service
public class FirmRpcService {
    private static final Logger logger = LoggerFactory.getLogger(FirmRpcService.class);
    @Autowired(required = false)
    FirmRpc firmRpc;

    /**
     * 查询所有市场
     *
     * @return
     */
    public List<Firm> findAllFirm() {

        FirmDto dto = TraceUtil.newDTO(FirmDto.class);
        dto.setDeleted(false);

        try {
            BaseOutput<List<Firm>> out = this.firmRpc.listByExample(dto);
            return StreamEx.ofNullable(out).filter(BaseOutput::isSuccess).map(BaseOutput::getData)
                    .findFirst()
                    .orElse(Lists.newArrayList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Lists.newArrayList();

    }
    /**
     * 查询当前市场
     *
     * @param firmId
     * @return
     */
    public Firm getFirmByIdOrEx(Long firmId) {
        return this.getFirmById(firmId).orElseThrow(()->{
            return new TraceBizException("查询市场失败");
        });
    }

    /**
     * 查询当前市场
     *
     * @param firmId
     * @return
     */
    public Optional<Firm> getFirmById(Long firmId) {
        if (firmId == null) {
            return Optional.empty();
        }
        BaseOutput<Firm> out = this.firmRpc.getById(firmId);
        if (out == null || !out.isSuccess()) {
            return Optional.empty();
        }
        return Optional.ofNullable(out.getData());
    }

    /**
     * 根据市场编码查询指定用户市场
     *
     * @param firmCode
     * @return
     */
    public Optional<Firm> getFirmByCode(String firmCode) {
        if (StringUtils.isBlank(firmCode)) {
            return Optional.empty();
        }
        BaseOutput<Firm> out = this.firmRpc.getByCode(firmCode);
        if (out == null || !out.isSuccess()) {
            return Optional.empty();
        }
        return Optional.ofNullable(out.getData());
    }

    /**
     * 查询市场列表
     *
     * @param firmDto
     * @return
     */
    public Optional<List<Firm>> getFirms(FirmDto firmDto) {
        if (firmDto == null) {
            return Optional.empty();
        }
        BaseOutput<List<Firm>> out = firmRpc.listByExample(firmDto);
        if (out == null || !out.isSuccess()) {
            return Optional.empty();
        }
        return Optional.ofNullable(out.getData());
    }

}
