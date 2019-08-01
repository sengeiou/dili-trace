package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.CustomerMapper;
import com.dili.trace.domain.Customer;
import com.dili.trace.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-31 14:56:14.
 */
@Service

public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    public CustomerMapper getActualDao() {
        return (CustomerMapper)getDao();
    }

    @Override
    public Customer findByCustomerId(String customerId) {
        List<Customer> customerList = getActualDao().findByCustomerId(customerId);
        if(customerList.size()>0){
            return customerList.get(0);
        }
        return null;
    }

    @Override
    public Customer findByPrintingCard(String printingCard) {
        List<Customer> customerList = getActualDao().findByPrintingCard(printingCard);
        if(customerList.size()>0){
            return customerList.get(0);
        }
        return null;
    }
}