package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.trace.domain.FieldConfig;
import com.dili.trace.domain.FieldConfigDetail;
import com.dili.trace.dto.ret.FieldConfigDetailRetDto;
import one.util.streamex.StreamEx;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * 保存或更新字段配置信息
     *
     * @param fieldConfigInput
     * @param fieldConfigDetailListInput
     * @return
     */
    public int saveOrUpdateFieldConfig(FieldConfig fieldConfigInput, List<FieldConfigDetail> fieldConfigDetailListInput) {
        if (fieldConfigInput == null || fieldConfigInput.getModuleType() == null || fieldConfigInput.getMarketId() == null) {
            throw new TraceBizException("参数错误");
        }

        FieldConfig fieldConfigItem = this.fieldConfigService.saveOrFind(fieldConfigInput.getMarketId(), fieldConfigInput.getModuleType());

        List<FieldConfigDetail> fieldConfigDetailList = StreamEx.ofNullable(fieldConfigDetailListInput).flatCollection(Function.identity())
                .nonNull().map(fc -> {
                    fc.setId(null);
                    fc.setIsValid(YesOrNoEnum.YES.getCode());
                    fc.setCreated(LocalDateTime.now());
                    fc.setModified(LocalDateTime.now());
                    fc.setFieldConfigId(fieldConfigItem.getId());
                    return fc;
                }).toList();
        if (fieldConfigDetailList.isEmpty()) {
            throw new TraceBizException("参数错误");
        }

        //标注原来的isvalue为false
        FieldConfigDetail domain = new FieldConfigDetail();
        domain.setIsValid(YesOrNoEnum.NO.getCode());

        FieldConfigDetail condition = new FieldConfigDetail();
        condition.setFieldConfigId(fieldConfigItem.getId());
        this.updateSelectiveByExample(domain, condition);

        //插入新数据
        StreamEx.of(fieldConfigDetailList).forEach(fc -> {
            this.insertSelective(fc);
        });
        //更新config的时间
        FieldConfig updatableFieldConfig = new FieldConfig();
        updatableFieldConfig.setId(fieldConfigItem.getId());
        updatableFieldConfig.setModified(LocalDateTime.now());
        return this.fieldConfigService.updateSelective(updatableFieldConfig);
    }

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
            q.setIsValid(YesOrNoEnum.YES.getCode());
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
