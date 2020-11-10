package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.CheckOrder;
import com.dili.trace.dto.CheckExcelDto;
import com.dili.trace.dto.CheckOrderDto;
import org.springframework.stereotype.Service;


/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/10/30
 */
@Service
public interface CheckBillService extends BaseService<CheckOrder, Long> {
    /**
     * 列表查询
     *
     * @param domain
     * @param useProvider
     * @return
     * @throws Exception
     */
    EasyuiPageOutput selectForEasyuiPage(CheckOrderDto domain, boolean useProvider) throws Exception;

    /**
     * 插入到check_data和image-cert表
     *
     * @param checkOrder
     * @param id
     */
    void insertOtherTable(CheckOrderDto checkOrder,Long id);

    /**
     * 更新check_data和image-cert表
     *
     * @param checkOrder
     */
    void updateOtherTable(CheckOrderDto checkOrder);

}
