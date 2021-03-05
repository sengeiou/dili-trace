package com.dili.trace.service;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.trace.domain.ProcessConfig;
import com.dili.trace.domain.RegisterBill;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;

/**
 * 流程配置参数
 */
@Service
public class ProcessConfigService extends TraceBaseService<ProcessConfig, Long> {

    /**
     * 查询 配置
     *
     * @param marketId
     * @return
     */
    public ProcessConfig findByMarketId(Long marketId) {

        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setIsAuditAfterRegist(YesOrNoEnum.YES.getCode());
        processConfig.setIsAuditBeforeCheckin(YesOrNoEnum.YES.getCode());
        processConfig.setIsWeightBeforeCheckin(YesOrNoEnum.NO.getCode());

        ProcessConfig pcq = new ProcessConfig();
        pcq.setMarketId(marketId);
        ProcessConfig item = StreamEx.of(this.listByExample(pcq)).findFirst().orElse(processConfig);

        return item;
    }

    public RegisterBill changeBillPropsByConfig(RegisterBill updatableBill, Long marketId) {
        ProcessConfig processConfig = this.findByMarketId(marketId);


        return updatableBill;

    }


}
