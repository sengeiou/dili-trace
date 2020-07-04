package com.dili.trace.service;

import java.lang.reflect.InvocationTargetException;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.RegisterBillHistory;
import com.dili.trace.glossary.YnEnum;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class RegisterBillHistoryService extends BaseServiceImpl<RegisterBillHistory, Long> {
    public RegisterBillHistory createHistory(RegisterBill bill) {
        RegisterBillHistory historyBill = new RegisterBillHistory();
        try {
            BeanUtils.copyProperties(historyBill, bill);
            historyBill.setId(null);
            historyBill.setBillId(bill.getId());
            this.insertSelective(historyBill);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TraceBusinessException("创建历史数据出错");
        }
        return historyBill;
    }
}