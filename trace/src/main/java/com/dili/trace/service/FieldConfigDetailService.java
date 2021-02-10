package com.dili.trace.service;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.trace.domain.DefaultFieldDetail;
import com.dili.trace.domain.FieldConfig;
import com.dili.trace.domain.FieldConfigDetail;
import com.dili.trace.dto.ret.FieldConfigDetailRetDto;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
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
     * 转换为json
     *
     * @param object
     * @return
     */
    private String toJsonString(Object object) {

        return JSON.toJSONString(object);
    }

    /**
     * 检测可用值是否正确
     *
     * @param value
     * @param fieldLabel
     * @param availableValues
     */
    private void checkValueAvailable(Object value, String fieldLabel, List<String> availableValues) {
        if (value != null && !availableValues.isEmpty()) {
            if (!availableValues.contains(String.valueOf(value))) {
                throw new TraceBizException("请填写正确的" + fieldLabel + "的值");
            }
        }
    }

    /**
     * 检测可显示以及必填
     *
     * @param value
     * @param fieldLabel
     * @param isDisplayed
     * @param isRequired
     * @return
     */
    private boolean checkAndIsSetDefaultValue(Object value, String fieldLabel, boolean isDisplayed, boolean isRequired) {
        if (isDisplayed && isRequired && value == null) {
            throw new TraceBizException("请填写" + fieldLabel);
        } else if (isRequired && value == null) {
            return true;
        }
        return false;
    }

    /**
     * 检查并设置默认值到object
     *
     * @param object
     * @param marketId
     * @param moduleType
     * @param <T>
     * @return
     */
    public <T> T checkAndSetValues(T object, Long marketId, Integer moduleType) {
        if (object == null) {
            return object;
        }
        String json = this.toJsonString(object);
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        List<FieldConfigDetailRetDto> fieldConfigDetailRetDtoList = this.findByMarketIdAndModuleType(marketId, moduleType);


        StreamEx.of(fieldConfigDetailRetDtoList).forEach(fcd -> {
            DefaultFieldDetail defaultFieldDetail = fcd.getDefaultFieldDetail();
            String jsonPath = defaultFieldDetail.getJsonPath();
            Integer xpathType = defaultFieldDetail.getJsonPathType();

            boolean isRequired = YesOrNoEnum.YES.getCode().equals(defaultFieldDetail.getIsRequired());
            boolean isDisplayed = YesOrNoEnum.YES.getCode().equals(defaultFieldDetail.getIsDisplayed());
            String defaultValue = defaultFieldDetail.getDefaultValue();
            String fieldLabel = defaultFieldDetail.getFieldLabel();
            List<String> availableValues = StreamEx.ofNullable(StringUtils.split(defaultFieldDetail.getAvailableValues(), ","))
                    .flatArray(Function.identity()).nonNull().map(String::valueOf).toList();
            if (fcd.getId() != null) {
                isRequired = YesOrNoEnum.YES.getCode().equals(fcd.getIsRequired());
                isDisplayed = YesOrNoEnum.YES.getCode().equals(fcd.getIsDisplayed());
            }

            if (xpathType == 0) {
                Object value = JsonPath.read(document, jsonPath);
                this.checkValueAvailable(value, fieldLabel, availableValues);
                if (this.checkAndIsSetDefaultValue(value, fieldLabel, isDisplayed, isRequired)) {
                    JsonPath.parse(json).set(jsonPath, defaultValue);
                }
            } else {
                Iterator ite = JsonPath.read(document, jsonPath);
                while (ite.hasNext()) {
                    Object value = ite.next();
                    this.checkValueAvailable(value, fieldLabel, availableValues);
                    if (this.checkAndIsSetDefaultValue(value, fieldLabel, isDisplayed, isRequired)) {
                        JsonPath.parse(json).set(jsonPath, defaultValue);
                    }
                }
            }

        });

        return object;
    }

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
            if (fd != null) {
                BeanUtils.copyProperties(df, ret);
            }
            ret.setDefaultFieldDetail(df);
            return ret;
        }).toList();

    }
}
