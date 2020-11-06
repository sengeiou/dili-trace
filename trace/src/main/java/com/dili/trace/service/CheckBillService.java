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
    EasyuiPageOutput selectForEasyuiPage(CheckOrderDto domain, boolean useProvider) throws Exception;
    CheckOrderDto selectAllInfoById(Long id);
    Integer insertOneToCheckOrder(CheckOrderDto checkOrder);
    void insertOtherTable(CheckOrderDto checkOrder,Long id);
    void updateOtherTable(CheckOrderDto checkOrder);
    void saveExcelData(CheckExcelDto checkExcelDto);
}
