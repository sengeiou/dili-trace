package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.SysConfig;

/**
 * @author asa.lee
 */
public interface SysConfigService extends BaseService<SysConfig, Long> {

    /**
     * 修改活跃度参数后立即刷新一次用户活跃度
     * @param query
     */
    void updateTraceReportLimitDay(SysConfig query);
}
