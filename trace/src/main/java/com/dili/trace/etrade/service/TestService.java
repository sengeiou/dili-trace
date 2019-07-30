package com.dili.trace.etrade.service;

import org.springframework.stereotype.Service;

import com.dili.ss.datasource.SwitchDataSource;

@Service
public class TestService {
	@SwitchDataSource("etradeDS")
	public String test() {
		return "";
	}
}
