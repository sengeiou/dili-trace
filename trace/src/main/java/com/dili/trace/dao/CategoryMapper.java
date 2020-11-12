package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.Category;

import java.util.List;

public interface CategoryMapper extends MyMapper<Category> {
    /**
     * 根据条件联合查询数据信息
     *
     * @param category
     * @return
     */
    List<Category> selectForPage(Category category);
}