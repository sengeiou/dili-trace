package com.dili.trace.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dili.trace.domain.Customer;
import com.dili.trace.service.CustomerService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-31 14:56:14.
 */
@Service

public class CustomerServiceImpl implements CustomerService {


    @Override
    public Customer findByCustomerId(String customerId) {

        return null;
    }

    @Override
    public Customer findByPrintingCard(String printingCard) {
      
        return null;
    }
	@Override
	public List<Customer> findByCustomerIdAndPrintingCard(Customer customer) {
		return Collections.emptyList();
	}
}