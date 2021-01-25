package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.SysConfig;
import com.dili.trace.enums.SysConfigTypeEnum;

/**
 * @author asa.lee
 */
public interface SysConfigService extends BaseService<SysConfig, Long> {

    /**
     * 修改活跃度参数后立即刷新一次用户活跃度
     * @param query
     */
    void updateTraceReportLimitDay(SysConfig query);

    /**
     * 返回开关，未配置或者不为y都返回false
     * @param optType
     * @param optCategory
     * @return
     */
    boolean isCallDataSwitch(String optType,String optCategory);
}
