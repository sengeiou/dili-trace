package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RUserCategory;
import com.dili.trace.service.RUserCategoryService;

import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableRetry
public class RUserCategoryServiceImpl extends BaseServiceImpl<RUserCategory,Long> implements RUserCategoryService {

    @Transactional
    @Override
    public BaseOutput dealBatchAdd(List<RUserCategory> rUserCategoryList,Long userId) {
        for (RUserCategory rUserCategory : rUserCategoryList) {
            if (rUserCategory.getUserId() == null) {
                rUserCategory.setUserId(userId);
            }

            deleteByExample(rUserCategory);
        }
        int rows = batchInsert(rUserCategoryList);

        return rows > 0 ? BaseOutput.success() : BaseOutput.failure();
    }
}
