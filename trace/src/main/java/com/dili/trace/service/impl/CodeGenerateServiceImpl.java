package com.dili.trace.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.CodeGenerateMapper;
import com.dili.trace.domain.CodeGenerate;
import com.dili.trace.service.CodeGenerateService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CodeGenerateServiceImpl extends BaseServiceImpl<CodeGenerate, Long>
		implements CodeGenerateService, CommandLineRunner {
	private static final String TRADE_REQUEST_CODE_TYPE = "TRADE_REQUEST_CODE";
	private CodeGenerateMapper getMapper() {
		return (CodeGenerateMapper) this.getDao();
	}



	private boolean checkAndInitTradeRequestCode() {

		CodeGenerate codeGenerate = this.getMapper().selectByTypeForUpdate(TRADE_REQUEST_CODE_TYPE).stream().findFirst()
				.orElse(DTOUtils.newDTO(CodeGenerate.class));
		codeGenerate.setPattern("yyyyMMddHH");
		codeGenerate.setType(TRADE_REQUEST_CODE_TYPE);
		codeGenerate.setPrefix("HZSY");
		if (codeGenerate.getId() == null) {
			LocalDateTime now = LocalDateTime.now();

			String nextSegment = now.format(DateTimeFormatter.ofPattern(codeGenerate.getPattern()));
			codeGenerate.setSegment(nextSegment);
			codeGenerate.setSeq(0L);
			this.insertSelective(codeGenerate);
		}
		return true;
	}



	@Transactional(propagation = Propagation.REQUIRED)
	public String nextTradeRequestCode() {

		return this.nextCode(TRADE_REQUEST_CODE_TYPE, 5);
	}
	private String nextCode(String codeType,int paddingSize) {

		CodeGenerate codeGenerate = this.getMapper().selectByTypeForUpdate(TRADE_REQUEST_CODE_TYPE).stream().findFirst()
				.orElse(null);
		if (codeGenerate == null) {
			throw new TraceBizException("生成编号错误");
		}

		// 时间比较
		LocalDateTime now = LocalDateTime.now();

		String nextSegment = now.format(DateTimeFormatter.ofPattern(codeGenerate.getPattern()));
		if (!nextSegment.equals(codeGenerate.getSegment())) {

			codeGenerate.setSeq(1L);
			codeGenerate.setSegment(nextSegment);
			codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));

		} else {

			codeGenerate.setSeq(codeGenerate.getSeq() + 1);
			codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));

		}

		this.updateSelective(codeGenerate);

		return StringUtils.trimToEmpty(codeGenerate.getPrefix()).concat(nextSegment)
				.concat(StringUtils.leftPad(String.valueOf(codeGenerate.getSeq()), paddingSize, "0"));
	}

	@Override
	public void run(String... args) throws Exception {
		this.checkAndInitTradeRequestCode();
	}

}
