package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Customer;
import com.dili.trace.domain.User;
import com.dili.trace.service.CustomerService;
import com.dili.trace.service.UserService;
import com.diligrp.manage.sdk.session.SessionContext;

import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeInfoController.class);

	@Autowired
	private CustomerService customerService;
	@Resource
	private UserService userService;

	/**
	 * 根据客户账号获取
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation("根据客户账号获取")
	@RequestMapping(value = "/id/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Customer> findByCustomerId(@PathVariable String id) {
		Customer customer = customerService.findByCustomerId(id);
		if (customer != null) {
			return BaseOutput.success().setData(this.maskCustomer(customer));

		} else {
			return BaseOutput.failure();
		}
	}

	/**
	 * 根据客户账号获取
	 * 
	 * @param printingCard
	 * @return
	 */
	@ApiOperation("根据客户账号获取")
	@RequestMapping(value = "/card/{printingCard}", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Customer> findByPrintingCard(@PathVariable String printingCard) {
		Customer customer = customerService.findByPrintingCard(printingCard);
		if (customer != null) {
			return BaseOutput.success().setData(this.maskCustomer(customer));

		} else {
			return BaseOutput.failure();
		}
	}

	/**
	 * 根据客户账号获取
	 * 
	 * @param tallyAreaNo
	 * @return
	 */
	@ApiOperation("根据理货区号获取客户获取")
	@RequestMapping(value = "/tallyAreaNo/{tallyAreaNo}", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<User> findTallyAreaNo(@PathVariable String tallyAreaNo) {
		User customer = userService.findByTaillyAreaNo(tallyAreaNo);
		if (customer != null) {
			return BaseOutput.success().setData(this.maskUser(customer));
		} else {
			return BaseOutput.failure();
		}
	}

	private User maskUser(User user) {
		if (SessionContext.hasAccess("post", "registerBill/create.html#user")) {
			return user;
		} else {
			String idNo = StringUtils.trimToEmpty(user.getCardNo());
			String phone = StringUtils.trimToEmpty(user.getPhone());
			String addr = StringUtils.trimToEmpty(user.getAddr());

			if (idNo.length() == 16) {
				idNo=replace(idNo, 8, 5, "*");
			}else if(idNo.length()==18) {
				idNo=replace(idNo, 10, 5, "*");
			}

			if(addr.length()>0) {
				addr=replace(addr, (addr.length()-1)/3,  (addr.length()-1)/2, "*");
			}
			user.setCardNo(idNo);
			user.setAddr(addr);
			return user;
		}

	}

	
	private Customer maskCustomer(Customer customer) {
		if (SessionContext.hasAccess("post", "registerBill/create.html#user")) {
			return customer;
		} else {
			String idNo = StringUtils.trimToEmpty(customer.getIdNo());
			String phone = StringUtils.trimToEmpty(customer.getPhone());
			String addr = StringUtils.trimToEmpty(customer.getAddress());

			if (idNo.length() == 16) {
				idNo=replace(idNo, 8, 5, "*");
			}else if(idNo.length()==18) {
				idNo=replace(idNo, 10, 5, "*");
			}

			if(addr.length()>0) {
				addr=replace(addr, (addr.length()-1)/3,  (addr.length()-1)/2, "*");
			}
			customer.setIdNo(idNo);
			customer.setAddress(addr);
			return customer;
		}

	}

	private String replace(String str,int startIndex,int length,String replacement) {
		
		if(str!=null&&str.length()>startIndex) {
			StringBuilder sb=new StringBuilder();
			for(int i=0;i<str.length();i++) {
				char ch=str.charAt(i);
				if(i>=startIndex&&i<=(startIndex+length)) {
					sb.append(replacement);
				}else {
					sb.append(ch);	
				}
			}
			
			return sb.toString();
		}
		return str;
		
	}
	
}
