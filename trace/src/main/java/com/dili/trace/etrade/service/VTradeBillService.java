package com.dili.trace.etrade.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.datasource.SwitchDataSource;
import com.dili.trace.etrade.domain.VTradeBill;


@Service
public class VTradeBillService extends BaseServiceImpl<VTradeBill, Long>{
	@SwitchDataSource("etradeDS")
	@Override
	public List<VTradeBill> listByExample(VTradeBill example) {
		
		return super.listByExample(example);
	}
}
