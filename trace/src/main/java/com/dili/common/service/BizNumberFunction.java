package com.dili.common.service;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.uid.domain.BizNumberRule;
import com.dili.ss.uid.service.BizNumberService;
import com.dili.trace.glossary.BizNumberType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @Author guzman.liu
 * @Description
 * 单号生成
 * @Date 2020/11/23 10:23
 * @return
 */
@Component
public class BizNumberFunction {
    @Autowired
    private BizNumberService bizNumberService;

    /**
     * 获取枚举获取业务号
     * @param bizNumberType
     * @return
     */
    public String getBizNumberByType(BizNumberType bizNumberType){
        return bizNumberService.getBizNumberByType(toBizNumberRule(bizNumberType));
    }

    /**
     * 枚举转BizNumberRule
     * @param bizNumberType
     * @return
     */
    private BizNumberRule toBizNumberRule(BizNumberType bizNumberType){
        BizNumberRule bizNumberRule = DTOUtils.newDTO(BizNumberRule.class);
        BeanUtils.copyProperties(bizNumberType, bizNumberRule);
        return bizNumberRule;
    }


}
