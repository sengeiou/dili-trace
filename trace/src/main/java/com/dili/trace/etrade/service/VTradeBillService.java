package com.dili.trace.etrade.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.datasource.SwitchDataSource;
import com.dili.ss.domain.BasePage;
import com.dili.trace.etrade.dao.VTradeBillMapper;
import com.dili.trace.etrade.domain.VTradeBill;
import com.dili.trace.etrade.domain.dto.VTradeBillQueryDTO;

@Service
public class VTradeBillService extends BaseServiceImpl<VTradeBill, Long> {
	@SwitchDataSource("etradeDS")
	@Override
	public List<VTradeBill> listByExample(VTradeBill example) {

		return super.listByExample(example);
	}

	public VTradeBillMapper getDao() {
		return (VTradeBillMapper) super.getDao();

	}

	@SwitchDataSource("etradeDS")
	public List<VTradeBill> selectTopRemoteData(VTradeBillQueryDTO dto) {
		if (dto.getPage() == null || dto.getPage() < 1) {
			dto.setPage(1);
		}
		if (dto.getRows() == null || dto.getRows() <= 0) {
			dto.setRows(10);
		}

		return this.getDao().selectTopRemoteData(dto);

	}

	@SwitchDataSource("etradeDS")
	public VTradeBill selectRemoteLatestData() {

		return this.getDao().selectRemoteLatestData();
	}

}
