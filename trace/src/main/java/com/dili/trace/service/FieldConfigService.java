package com.dili.trace.service;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.trace.domain.FieldConfig;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 字段配置信息
 */
@Service
public class FieldConfigService extends TraceBaseService<FieldConfig, Long> {
    /**
     * 查询字段配置信息
     *
     * @param marketId
     * @param moduleType
     * @return
     */
    public Optional<FieldConfig> findByMarketIdAndModuleType(Long marketId, Integer moduleType) {
        if (marketId == null || moduleType == null) {
            return Optional.empty();
        }
        FieldConfig q = new FieldConfig();
        q.setMarketId(marketId);
        q.setModuleType(moduleType);
        q.setIsValid(YesOrNoEnum.YES.getCode());
        return StreamEx.of(this.listByExample(q)).findFirst();
    }
}
