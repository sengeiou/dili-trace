package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Customer;
import com.dili.trace.domain.User;
import com.dili.trace.service.CustomerService;
import com.dili.trace.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 寿光sqlserver库中相关基础信息
 */
@RestController
@RequestMapping(value = "/trade/customer")
public class TradeInfoController {
    private static final Logger LOGGER= LoggerFactory.getLogger(TradeInfoController.class);

    @Autowired
    private CustomerService customerService;
    @Resource
    private UserService userService;

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
    /**
     * 根据客户账号获取
     * @param tallyAreaNo
     * @return
     */
    @ApiOperation("根据理货区号获取客户获取")
    @RequestMapping(value = "/tallyAreaNo/{tallyAreaNo}",method = {RequestMethod.GET, RequestMethod.POST})
    public BaseOutput<User> findTallyAreaNo( @PathVariable String tallyAreaNo){
        User customer = userService.findByTaillyAreaNo(tallyAreaNo);
        if(customer!=null){
            return BaseOutput.success().setData(customer);
        }else {
            return BaseOutput.failure();
        }
    }
}
