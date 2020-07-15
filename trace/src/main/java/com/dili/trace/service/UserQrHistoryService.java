package com.dili.trace.service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.UserQrHistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class UserQrHistoryService extends BaseServiceImpl<UserQrHistory, Long> implements CommandLineRunner {
	@Autowired
	UserService userService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	RUserUpStreamService rUserUpStreamService;
	@Autowired
	RegisterBillMapper registerBillMapper;

	 
	public void run(String... args) {
	
	}

}