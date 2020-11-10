package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.CheckOrderDispose;
import com.dili.trace.dto.CheckOrderDisposeDto;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/11/3
 */
public interface CheckOrderDisposeService extends BaseService<CheckOrderDispose, Long> {
    /**
     * 列表查询
     *
     * @param domain
     * @param useProvider
     * @return
     * @throws Exception
     */
    EasyuiPageOutput selectForEasyuiPage(CheckOrderDisposeDto domain, boolean useProvider) throws Exception;
}
