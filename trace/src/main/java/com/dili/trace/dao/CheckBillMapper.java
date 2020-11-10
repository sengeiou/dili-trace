package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.CheckOrder;
import com.dili.trace.dto.CheckExcelDto;
import com.dili.trace.dto.CheckOrderDto;

import java.util.List;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/10/30
 */
public interface CheckBillMapper extends MyMapper<CheckOrder> {
    /**
     * 根据条件联合查询数据信息
     *
     * @param checkOrder
     * @return
     */
    List<CheckOrder> selectForPage(CheckOrderDto checkOrder);
}
