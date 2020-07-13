package com.dili.trace.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.RegisterBillHistory;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterBillHistoryService extends BaseServiceImpl<RegisterBillHistory, Long> {
	@Autowired
	RegisterBillService billService;

    public RegisterBillHistory createHistory(Long billId) {
    	RegisterBill billItem=this.billService.get(billId);
        RegisterBillHistory historyBill = new RegisterBillHistory();
        try {
            BeanUtils.copyProperties(historyBill, billItem);
            historyBill.setId(null);
            historyBill.setBillId(billId);
            // historyBill.setModified(new Date());
            this.insertSelective(historyBill);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TraceBusinessException("创建历史数据出错");
        }
        return historyBill;
    }
}