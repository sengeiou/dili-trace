package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.domain.RUserCategory;
import com.dili.trace.enums.MessageStateEnum;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface RUserCategoryService extends BaseService<RUserCategory,Long> {
    public BaseOutput dealBatchAdd(List<RUserCategory> rUserCategoryList,Long userId);
}
