package com.dili.trace.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.UserHistory;
import com.dili.trace.dto.GroupByProductReportDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillReportQueryDto;
import com.dili.trace.service.RegisterBillReportService;

@Service
public class RegisterBillReportServiceImpl implements RegisterBillReportService{
	@Autowired RegisterBillMapper mapper;
	public EasyuiPageOutput listPageGroupByProduct(RegisterBillReportQueryDto dto) throws Exception {
		if(dto.getPage()==null) {
			dto.setPage(1);
		}
		if(dto.getRows()==null) {
			dto.setRows(10);
		}
		long total = 10;
		List<GroupByProductReportDto>list=this.mapper.listPageGroupByProduct(dto);
		List results = ValueProviderUtils.buildDataByProvider(dto, list);
		return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results);
	}

}
