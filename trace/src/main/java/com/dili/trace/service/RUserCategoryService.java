package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RUserCategory;

import java.util.List;

/**
 * 客户品类关系
 */
public interface RUserCategoryService extends BaseService<RUserCategory, Long> {
    /**
     * 删除关系
     *
     * @param rUserCategoryList
     * @param userId
     * @return
     */
    public BaseOutput dealBatchAdd(List<RUserCategory> rUserCategoryList, Long userId);
}
