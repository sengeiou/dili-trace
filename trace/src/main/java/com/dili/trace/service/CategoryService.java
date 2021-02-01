package com.dili.trace.service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.hangguo.HangGuoCategory;
import org.springframework.stereotype.Service;

/**
 * @author asa.lee
 */
@Service
public class CategoryService extends BaseServiceImpl<HangGuoCategory, Long> {
    /**
     * 保存category
     *
     * @param id
     * @param marektId
     * @return
     */
    public HangGuoCategory saveCategory(Long id, Long marektId) {
        LOGGER.debug("saveCategory id={},marketId={}", id, marektId);
        return null;
    }

}
