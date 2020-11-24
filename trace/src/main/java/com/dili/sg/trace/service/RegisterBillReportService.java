package com.dili.sg.trace.service;

import java.util.List;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.dto.GroupByProductReportDto;
import com.dili.trace.dto.RegisterBillReportQueryDto;

public interface RegisterBillReportService {

	public EasyuiPageOutput listPageGroupByProduct(RegisterBillReportQueryDto dto) throws Exception;

	public List<GroupByProductReportDto> listGroupByProduct(RegisterBillReportQueryDto dto)
			throws Exception;

	public EasyuiPageOutput listPageGroupByNameAndPlate(RegisterBillReportQueryDto dto) throws Exception;

	public List<GroupByProductReportDto> listGroupByNameAndPlate(RegisterBillReportQueryDto dto)
			throws Exception;
	
	
	public EasyuiPageOutput listPageGroupByOrigin(RegisterBillReportQueryDto dto) throws Exception;

	public List<GroupByProductReportDto> listGroupByOrigin(RegisterBillReportQueryDto dto)
			throws Exception;
	
	public GroupByProductReportDto summaryGroup(RegisterBillReportQueryDto dto) ;
}
