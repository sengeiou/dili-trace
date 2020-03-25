package com.dili.trace.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
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
		dto=this.checkAndSetPageParams(dto);
		dto.setGroupByColumns(String.join(",",Arrays.asList("product_id,product_name")));
		Long total = this.mapper.listPageGroupByProductCount(dto);
		List<GroupByProductReportDto>list=this.mapper.listPageGroupByProduct(dto);
		List results = ValueProviderUtils.buildDataByProvider(dto, list);
		return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total==null?0:total)), results);
	}
	@Override
	public List<GroupByProductReportDto> listGroupByProduct(RegisterBillReportQueryDto dto) throws Exception {
		dto.setGroupByColumns(String.join(",",Arrays.asList("product_id,product_name")));
		List<GroupByProductReportDto>list=this.mapper.listPageGroupByProduct(dto);
		return list;
	}
	
	@Override
	public EasyuiPageOutput listPageGroupByNameAndPlate(RegisterBillReportQueryDto dto) throws Exception {
		dto=this.checkAndSetPageParams(dto);
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
		dto=this.checkAndSetPageParams(dto);
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
	@Override
	public GroupByProductReportDto summaryGroup(RegisterBillReportQueryDto dto) {
		return this.mapper.summaryGroup(dto);
	}
	private RegisterBillReportQueryDto checkAndSetPageParams(RegisterBillReportQueryDto dto) {
		
		if(dto.getPage()==null||dto.getPage()<=0) {
			dto.setPage(1);
		}
		if(dto.getRows()==null||dto.getRows()<=0) {
			dto.setRows(10);
		}
		dto.setOffSet((dto.getPage()-1)*dto.getRows());
		if(StringUtils.isNotBlank(dto.getSort())) {
			String sort=Stream.of(dto.getSort().split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(str->{
				StringBuffer sb = new StringBuffer();
				Pattern p = Pattern.compile("w*[A-Z]w*");
				Matcher m = p.matcher(str);
				while (m.find())
				{ // Find each match in turn; String can't do this.
				//String name = m.group(1); // Access a submatch group; String can't do this.
				m.appendReplacement(sb,"_"+m.group().toLowerCase());
				System.out.println("m.group() is= " + m.group());
				}
				m.appendTail(sb);
				return sb.toString();
				
			}).collect(Collectors.joining(","));
			dto.setSort(sort);
		}
		return dto;
		
		
		
	}
	public static void main(String[] args) {
		String in = "aBcABDl";
		System.out.println("in is= " + in);
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("w*[A-Z]w*");
		Matcher m = p.matcher(in);
		while (m.find())
		{ // Find each match in turn; String can't do this.
		//String name = m.group(1); // Access a submatch group; String can't do this.
		m.appendReplacement(sb,"_"+m.group().toLowerCase());
		System.out.println("m.group() is= " + m.group());
		}
		m.appendTail(sb);
		System.out.println("sb is= " + sb);
	}


}
