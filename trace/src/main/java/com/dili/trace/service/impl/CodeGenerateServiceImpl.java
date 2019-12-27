package com.dili.trace.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.exception.AppException;
import com.dili.trace.dao.CodeGenerateMapper;
import com.dili.trace.domain.CodeGenerate;
import com.dili.trace.service.CodeGenerateService;

@Service
public class CodeGenerateServiceImpl extends BaseServiceImpl<CodeGenerate, Long> implements CodeGenerateService {
	private static final String SAMPLE_CODE_TYPE = "SAMPLE_CODE";

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
		LocalDateTime modified = codeGenerate.getModified().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		LocalDateTime now = LocalDateTime.now();

		Long nextSeq=1L;
		String nextSegment=codeGenerate.getSegment();
		if (modified.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
				.equals(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
			nextSeq=codeGenerate.getSeq() + 1;
			nextSegment=now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		codeGenerate.setSeq(nextSeq);
		codeGenerate.setSegment(nextSegment);
		codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));

		this.updateSelective(codeGenerate);

		return StringUtils.trimToEmpty(codeGenerate.getSuffix())
				.concat(nextSegment)
				.concat(StringUtils.leftPad(String.valueOf(nextSeq), 5, "0"));
	}
	

}
