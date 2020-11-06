package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.CheckOrderDispose;
import com.dili.trace.dto.CheckOrderDisposeDto;

import java.util.List;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/11/3
 */
public interface CheckOrderDisposeMapper extends MyMapper<CheckOrderDispose> {
    /**
     * 根据条件联合查询数据信息
     *
     * @param checkOrderDisposeDto
     * @return
     */
    List<CheckOrderDispose> selectForPage(CheckOrderDisposeDto checkOrderDisposeDto);
}
