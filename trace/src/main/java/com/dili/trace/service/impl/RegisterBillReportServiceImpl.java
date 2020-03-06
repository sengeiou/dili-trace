package com.dili.trace.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dto.GroupByProductReportDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.RegisterBillReportService;

@Service
public class RegisterBillReportServiceImpl implements RegisterBillReportService{
	@Autowired RegisterBillMapper mapper;
	public List<GroupByProductReportDto> listPageGroupByProduct() {
		return this.mapper.listPageGroupByProduct(DTOUtils.newDTO(RegisterBillDto.class));
	}

}
