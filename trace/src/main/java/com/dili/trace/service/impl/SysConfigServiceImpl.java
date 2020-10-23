package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.SysConfig;
import com.dili.trace.service.SysConfigService;
import com.dili.trace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
}
