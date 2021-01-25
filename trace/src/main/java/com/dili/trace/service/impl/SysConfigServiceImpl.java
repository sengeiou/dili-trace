package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.SysConfig;
import com.dili.trace.enums.SysConfigTypeEnum;
import com.dili.trace.service.SysConfigService;
import com.dili.trace.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


/**
 * @author asa.lee
 */

@Transactional(rollbackFor = Exception.class)
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig, Long> implements SysConfigService {


    @Autowired
    UserService userService;

    @Override
    public void updateTraceReportLimitDay(SysConfig query) {
        updateSelective(query);
        userService.updateUserActiveByTime();
    }

    @Override
    public boolean isCallDataSwitch(String optType, String optCategory) {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setOptType(SysConfigTypeEnum.PUSH_DATA_SUBJECT.getCode());
        sysConfig.setOptCategory(SysConfigTypeEnum.PUSH_DATA_CATEGORY.getCode());
        List<SysConfig> list = this.listByExample(sysConfig);
        SysConfig config = new SysConfig();
        if (CollectionUtils.isNotEmpty(list)) {
            config= list.get(0);
        }
        return Objects.isNull(config)||!SysConfigTypeEnum.SWITCH_OPEN_VAL.getCode().equals(config.getOptValue());
    }
}
