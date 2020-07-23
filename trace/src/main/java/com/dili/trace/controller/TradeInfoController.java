package com.dili.trace.controller;

import java.util.List;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Customer;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.util.MaskUserInfo;
import com.diligrp.manage.sdk.session.SessionContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 寿光sqlserver库中相关基础信息
 */
@RestController
@RequestMapping(value = "/trade/customer")
public class TradeInfoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeInfoController.class);


	@Autowired
	private UserService userService;
	@Autowired
	private UserPlateService userPlateService;

	



	/**
	 * 根据客户ID获取车牌号
	 * 
	 * @param tallyAreaNo
	 * @return
	 */
	@ApiOperation("根据理货区号获取客户获取")
	@RequestMapping(value = "/findUserPlateByUserId", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<UserPlate>> findUserPlateByUserId(Long userId) {

		List<UserPlate> list = this.userPlateService.findUserPlateByUserId(userId);

		return BaseOutput.success().setData(list);
	}

	private User maskUser(User user) {
		if (SessionContext.hasAccess("post", "registerBill/create.html#user")) {
			return user;
		} else {
			user.setCardNo(MaskUserInfo.maskIdNo(user.getCardNo()));
			user.setAddr(MaskUserInfo.maskAddr(user.getAddr()));
			return user;
		}

	}

	private Customer maskCustomer(Customer customer) {
		if (SessionContext.hasAccess("post", "registerBill/create.html#user")) {
			return customer;
		} else {
			customer.setIdNo(MaskUserInfo.maskIdNo(customer.getIdNo()));
			customer.setAddress(MaskUserInfo.maskAddr(customer.getAddress()));
			return customer;
		}

	}

}
