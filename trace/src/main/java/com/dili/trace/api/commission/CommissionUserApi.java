package com.dili.trace.api.commission;

import com.dili.trace.domain.User;
import com.dili.trace.service.SMSService;
import com.dili.trace.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;
import com.dili.common.exception.TraceBizException;

/**
 * 委托单接口
 */
@RestController
@RequestMapping(value = "/api/commission/commissionUserApi")
public class CommissionUserApi {
	private static final Logger logger = LoggerFactory.getLogger(CommissionUserApi.class);
	@Autowired
	UserServiceImpl userService;


}
