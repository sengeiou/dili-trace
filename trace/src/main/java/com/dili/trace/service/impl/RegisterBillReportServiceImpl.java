package com.dili.trace.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dto.GroupByProductReportDto;
import com.dili.trace.dto.RegisterBillReportQueryDto;
import com.dili.trace.service.RegisterBillReportService;

@Service
public class RegisterBillReportServiceImpl implements RegisterBillReportService{
	@Autowired RegisterBillMapper mapper;
	public EasyuiPageOutput listPageGroupByProduct(RegisterBillReportQueryDto dto) throws Exception {
		if(dto.getPage()==null||dto.getPage()<=0) {
			dto.setPage(1);
		}
		if(dto.getRows()==null||dto.getRows()<=0) {
			dto.setRows(10);
		}
		dto.setOffSet((dto.getPage()-1)*dto.getRows());
		dto.setGroupByColumns(String.join(",",Arrays.asList("product_name")));
		Long total = this.mapper.listPageGroupByProductCount(dto);
		List<GroupByProductReportDto>list=this.mapper.listPageGroupByProduct(dto);
		List results = ValueProviderUtils.buildDataByProvider(dto, list);
		return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total==null?0:total)), results);
	}
	@Override
	public List<GroupByProductReportDto> listGroupByProduct(RegisterBillReportQueryDto dto) throws Exception {
		dto.setGroupByColumns(String.join(",",Arrays.asList("product_name")));
		List<GroupByProductReportDto>list=this.mapper.listPageGroupByProduct(dto);
		return list;
	}
	
	@Override
	public EasyuiPageOutput listPageGroupByNameAndPlate(RegisterBillReportQueryDto dto) throws Exception {
		if(dto.getPage()==null||dto.getPage()<=0) {
			dto.setPage(1);
		}
		if(dto.getRows()==null||dto.getRows()<=0) {
			dto.setRows(10);
		}
		dto.setOffSet((dto.getPage()-1)*dto.getRows());
		dto.setGroupByColumns(String.join(",",Arrays.asList("name,plate")));
		Long total = this.mapper.listPageGroupByProductCount(dto);
		List<GroupByProductReportDto>list=this.mapper.listPageGroupByProduct(dto);
		List results = ValueProviderUtils.buildDataByProvider(dto, list);
		return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total==null?0:total)), results);
	}
	@Override
	public List<GroupByProductReportDto> listGroupByNameAndPlate(RegisterBillReportQueryDto dto) throws Exception {
		dto.setGroupByColumns(String.join(",",Arrays.asList("name,plate")));
		List<GroupByProductReportDto>list=this.mapper.listPageGroupByProduct(dto);
		return list;
	}
	@Override
	public EasyuiPageOutput listPageGroupByOrigin(RegisterBillReportQueryDto dto) throws Exception {
		if(dto.getPage()==null||dto.getPage()<=0) {
			dto.setPage(1);
		}
		if(dto.getRows()==null||dto.getRows()<=0) {
			dto.setRows(10);
		}
		dto.setOffSet((dto.getPage()-1)*dto.getRows());
		dto.setGroupByColumns(String.join(",",Arrays.asList("origin_name")));
		Long total = this.mapper.listPageGroupByProductCount(dto);
		List<GroupByProductReportDto>list=this.mapper.listPageGroupByProduct(dto);
		List results = ValueProviderUtils.buildDataByProvider(dto, list);
		return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total==null?0:total)), results);
	}
	@Override
	public List<GroupByProductReportDto> listGroupByOrigin(RegisterBillReportQueryDto dto) throws Exception {
		dto.setGroupByColumns(String.join(",",Arrays.asList("origin_name")));
		List<GroupByProductReportDto>list=this.mapper.listPageGroupByProduct(dto);
		return list;
	}


}
