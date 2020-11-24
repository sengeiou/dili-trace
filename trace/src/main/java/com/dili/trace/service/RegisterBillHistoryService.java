package com.dili.trace.service;

import java.lang.reflect.InvocationTargetException;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.RegisterBillHistory;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @Author guzman.liu
 * @Description
 * @Date 2020/11/23 13:58
 */
@Service
public class RegisterBillHistoryService extends BaseServiceImpl<RegisterBillHistory, Long> {
	@Autowired
	RegisterBillService registerBillService;

	/**
	 *
	 * @Author guzman.liu
	 * @Description
	 * @Date 2020/11/23 13:58
	 */
    public RegisterBillHistory createHistory(Long billId) {
    	RegisterBill billItem=this.registerBillService.get(billId);
        RegisterBillHistory historyBill = new RegisterBillHistory();
        try {
            BeanUtils.copyProperties(historyBill, billItem);
            historyBill.setId(null);
            historyBill.setBillId(billId);
            // historyBill.setModified(new Date());
            this.insertSelective(historyBill);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TraceBizException("创建历史数据出错");
        }
        return historyBill;
    }
}