package com.dili.trace.service;

import java.util.List;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.dto.GroupByProductReportDto;
import com.dili.trace.dto.RegisterBillReportQueryDto;

public interface RegisterBillReportService {

	public EasyuiPageOutput listPageGroupByProduct(RegisterBillReportQueryDto dto) throws Exception;
	
	public List<GroupByProductReportDto> listGroupByProduct(RegisterBillReportQueryDto dto) throws Exception;
}
