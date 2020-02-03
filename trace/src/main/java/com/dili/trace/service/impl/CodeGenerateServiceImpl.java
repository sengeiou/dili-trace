package com.dili.trace.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.trace.dao.CodeGenerateMapper;
import com.dili.trace.domain.CodeGenerate;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.service.CodeGenerateService;
import com.dili.trace.service.RegisterBillService;

@Service
public class CodeGenerateServiceImpl extends BaseServiceImpl<CodeGenerate, Long> implements CodeGenerateService {
	private static final String SAMPLE_CODE_TYPE = "SAMPLE_CODE";

	@Autowired
	RegisterBillService registerBillService;
	@PostConstruct
	public void init() {
		
		RegisterBill domain=DTOUtils.newDTO(RegisterBill.class);
		domain.setOrder("desc");
		domain.setSort("sample_code");
		domain.setPage(1);
		domain.setRows(1);
		RegisterBill registerBill=this.registerBillService.listByExample(domain).stream().findFirst().orElse(DTOUtils.newDTO(RegisterBill.class));
		String maxSampleCode=registerBill.getSampleCode();
		
		
		CodeGenerate codeGenerate = this.getMapper().selectByTypeForUpdate(SAMPLE_CODE_TYPE).stream().findFirst().orElse(DTOUtils.newDTO(CodeGenerate.class));
		codeGenerate.setPattern("yyyyMMdd");
		codeGenerate.setType(SAMPLE_CODE_TYPE);
		codeGenerate.setPrefix("c");
		
		if(codeGenerate.getId()==null) {
			if(StringUtils.isNotBlank(maxSampleCode)) {
				codeGenerate.setSegment(maxSampleCode.substring(1, 9));
				codeGenerate.setSeq(Long.valueOf(maxSampleCode.substring(9, 14)));
			}
			this.insertSelective(codeGenerate);
		}else {
			if(StringUtils.isNotBlank(maxSampleCode)) {
				String segment=maxSampleCode.substring(1, 9);
				Long seq=Long.valueOf(maxSampleCode.substring(9, 14));
				if(!segment.equals(codeGenerate.getSegment())||!seq.equals(codeGenerate.getSeq())) {
					codeGenerate.setSegment(segment);
					codeGenerate.setSeq(seq);
					this.updateSelective(codeGenerate);
				}
				
				
			}
			
			
			
		}
		
	}
	private CodeGenerateMapper getMapper() {

		return (CodeGenerateMapper) this.getDao();
	}


	@Transactional(propagation = Propagation.REQUIRED)
	public String nextSampleCode() {

		CodeGenerate codeGenerate = this.getMapper().selectByTypeForUpdate(SAMPLE_CODE_TYPE).stream().findFirst().orElse(null);
		if(codeGenerate==null) {
					throw new AppException("生成采样单编号错误");
		};
		// 时间比较
		LocalDateTime now = LocalDateTime.now();
		
		String nextSegment=now.format(DateTimeFormatter.ofPattern(codeGenerate.getPattern()));
		if(!nextSegment.equals(codeGenerate.getSegment())) {
			
			codeGenerate.setSeq(1L);
			codeGenerate.setSegment(nextSegment);
			codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
			
		}else {
			
			codeGenerate.setSeq(codeGenerate.getSeq()+1);
			codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
			
		}
		

		this.updateSelective(codeGenerate);

		return StringUtils.trimToEmpty(codeGenerate.getPrefix())
				.concat(nextSegment)
				.concat(StringUtils.leftPad(String.valueOf(codeGenerate.getSeq()), 5, "0"));
	}
	

}
