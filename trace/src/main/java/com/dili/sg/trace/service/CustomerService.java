package com.dili.sg.trace.service;

import cn.hutool.http.HttpUtil;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.sg.trace.rpc.CardResultDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.dili.ss.domain.BaseOutput;
import com.dili.sg.trace.domain.Customer;
import com.dili.sg.trace.rpc.CardQueryInput;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-31 14:56:14.
 */
@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    @Autowired(required = false)
    CustomerRpc customerRpc;
    @Autowired
    GlobalVarService globalVarService;

    @Value("${accountService.url}")
    private String accountServiceUrl;


    /**
     * 查询客户信息
     *
     * @param cust
     * @return
     */
    public Optional<Customer> findCustomer(Customer cust) {
        return this.listCustomers(cust.getCustomerId(), cust.getPrintingCard());
    }

    /**
     * 查询用户信息
     *
     * @param customerCode
     * @param cardNo
     * @return
     */
    private Optional<Customer> listCustomers(String customerCode, String cardNo) {
        if (StringUtils.isAllBlank(customerCode, cardNo)) {
            return Optional.empty();
        }
        return this.queryCardInfoByCustomerCode(customerCode, cardNo).map(card -> {

            Customer customer = new Customer();
            customer.setPrintingCard(card.getCardNo());
            customer.setCustomerId(customerCode);
            customer.setName(card.getCustomerName());
            customer.setIdNo(card.getCustomerCertificateNumber());

            Long customerId = card.getCustomerId();
            this.findCustomerById(customerId).ifPresent(cust -> {
                customer.setPhone(cust.getContactsPhone());
                customer.setAddress(cust.getCertificateAddr());
                customer.setCustomerId(cust.getCode());
            });
            return customer;

        });

    }

    /**
     * 查询用户信息
     *
     * @param customerId
     * @return
     */
    private Optional<CustomerExtendDto> findCustomerById(Long customerId) {
        if (customerId == null) {
            return Optional.empty();
        }
        CustomerQueryInput input = new CustomerQueryInput();
        input.setMarketId(this.globalVarService.getMarketId());
        input.setId(customerId);

        try {
            BaseOutput<List<CustomerExtendDto>> output = this.customerRpc.list(input);
            if (!output.isSuccess()) {
                return Optional.empty();
            }
            return StreamEx.ofNullable(output.getData()).nonNull()
                    .flatCollection(Function.identity()).nonNull()
                    .findFirst();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }


    /**
     * 查询卡信息
     *
     * @param customerCode
     * @throws IOException
     */
    private Optional<CardResultDto> queryCardInfoByCustomerCode(String customerCode, String cardNo) {
        CardQueryInput input = new CardQueryInput();
        input.setFirmId(this.globalVarService.getMarketId());
        input.setCustomerCode(StringUtils.trimToNull(customerCode));
        if (StringUtils.isNotBlank(cardNo)) {
            input.setCardNos(Lists.newArrayList(cardNo.trim()));
        }


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String jsonBody = mapper.writeValueAsString(input);
//            System.out.println(jsonBody);
            String respBody = HttpUtil.post(this.accountServiceUrl + "/api/account/getList", jsonBody);
//            System.out.println(respBody);
            BaseOutput<List<CardResultDto>> out = mapper.readValue(respBody, new TypeReference<BaseOutput<List<CardResultDto>>>() {
            });
            if (!"200".equals(out.getCode())) {
                return Optional.empty();
            }
            return StreamEx.ofNullable(out.getData()).nonNull().flatCollection(Function.identity()).nonNull().findFirst();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }


    /**
     * 测试
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        CustomerService s = new CustomerService();
        s.globalVarService = new GlobalVarService();
        s.accountServiceUrl = "http://gateway.diligrp.com:8285/account-service";
        Optional<CardResultDto> opt = s.queryCardInfoByCustomerCode("PC00008683", "888810026412");
        s.listCustomers("PC00008683", "888810026412").ifPresent(c -> {

            System.out.println(c);
        });
    }
}