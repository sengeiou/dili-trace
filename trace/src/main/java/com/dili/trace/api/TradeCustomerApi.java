package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Customer;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.TaskGetParam;
import com.dili.trace.service.CustomerService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 * //交易区的客户信息
 */
@RestController
@RequestMapping(value = "/api/trade/customer")
@Api(value ="/api/detect", description = "检测任务相关接口")
public class TradeCustomerApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeCustomerApi.class);
    @Autowired
    private CustomerService customerService;

    /**
     * 根据客户账号获取
     * @param id
     * @return
     */
    @ApiOperation("根据客户账号获取")
    @RequestMapping(value = "/id/{id}",method = {RequestMethod.GET, RequestMethod.POST})
    public BaseOutput<Customer> findByCustomerId( @PathVariable String id){
        Customer customer = customerService.findByCustomerId(id);
        if(customer!=null){
            return BaseOutput.success().setData(customer);
        }else {
            return BaseOutput.failure();
        }
    }
    /**
     * 根据客户账号获取
     * @param printingCard
     * @return
     */
    @ApiOperation("根据客户账号获取")
    @RequestMapping(value = "/card/{printingCard}",method = {RequestMethod.GET, RequestMethod.POST})
    public BaseOutput<Customer> findByPrintingCard( @PathVariable String printingCard){
        Customer customer = customerService.findByPrintingCard(printingCard);
        if(customer!=null){
            return BaseOutput.success().setData(customer);
        }else {
            return BaseOutput.failure();
        }
    }

}
