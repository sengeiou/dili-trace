package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.Customer;

import java.util.List;

public interface CustomerMapper extends MyMapper<Customer> {
    List<Customer> findByCustomerId(String customerId);
    List<Customer> findByPrintingCard(String printingCard);
}