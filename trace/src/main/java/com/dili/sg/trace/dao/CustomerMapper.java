package com.dili.sg.trace.dao;

import com.dili.sg.trace.domain.Customer;
import com.dili.ss.base.MyMapper;

import java.util.List;

public interface CustomerMapper extends MyMapper<Customer> {
    List<Customer> findByCustomerId(String customerId);
    List<Customer> findByPrintingCard(String printingCard);
    
    List<Customer> findByCustomerIdAndPrintingCard(Customer customer);
}