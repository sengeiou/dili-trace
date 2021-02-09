package com.dili.trace.service;

import com.dili.trace.domain.FieldConfigDetail;
import com.dili.trace.dto.ret.FieldConfigDetailRetDto;
import one.util.streamex.StreamEx;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 字段配置详情
 */
@Service
public class FieldConfigDetailService extends TraceBaseService<FieldConfigDetail, Long> {
    @Autowired
    DefaultFieldDetailService defaultFieldDetailService;

    @Autowired
    FieldConfigService fieldConfigService;

    /**
     * 查询字段配置详情
     *
     * @param marketId
     * @param moduleType
     * @return
     */
    public List<FieldConfigDetailRetDto> findByMarketIdAndModuleType(Long marketId, Integer moduleType) {
        Map<Long, FieldConfigDetail> fieldConfigDetailMap = StreamEx.of(this.fieldConfigService.findByMarketIdAndModuleType(marketId, moduleType)).map(fc -> {
            FieldConfigDetail q = new FieldConfigDetail();
            q.setFieldConfigId(fc.getId());
            return this.listByExample(q);
        }).flatCollection(Function.identity()).mapToEntry(FieldConfigDetail::getDefaultFieldDetailId, Function.identity()).toMap();


        return StreamEx.of(this.defaultFieldDetailService.findByModuleType(moduleType)).map(df -> {

            FieldConfigDetail fd = fieldConfigDetailMap.get(df.getId());

            FieldConfigDetailRetDto ret = new FieldConfigDetailRetDto();
            ret.setFieldId(df.getFieldId());
            ret.setFieldName(df.getFieldName());

            if (fd == null) {
                BeanUtils.copyProperties(df, ret);
            } else {
                BeanUtils.copyProperties(fd, ret);
            }
            return ret;
        }).toList();

    }
}
