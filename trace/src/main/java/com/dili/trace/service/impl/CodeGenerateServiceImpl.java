package com.dili.trace.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
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

	public String currentSampleCode() {
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String nextSampleCode() {

		CodeGenerate item = this.selectCodeGenerateByType(SAMPLE_CODE_TYPE).orElseGet(() -> {
			this.insertCodeGenerate("c", SAMPLE_CODE_TYPE);
			return this.selectCodeGenerateByType(SAMPLE_CODE_TYPE).get();
		});
		if (item == null) {
			throw new AppException("生成采样单编号错误");
		}
		CodeGenerate codeGenerate = this.getMapper().selectByIdForUpdate(item.getId()).stream().findFirst()
				.orElse(null);

		//时间比较
		LocalDateTime modified = codeGenerate.getModified().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		LocalDateTime now = LocalDateTime.now();

		if (modified.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
				.equals(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
			
			codeGenerate.setCode(codeGenerate.getCode()+1);
		}else {
			codeGenerate.setCode(1L);
		}

		codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
		
		this.updateSelective(codeGenerate);

		return StringUtils.trimToEmpty(codeGenerate.getSuffix()).concat(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).concat(StringUtils.leftPad(String.valueOf(codeGenerate.getCode()), 5, "0"));
	}

	private int insertCodeGenerate(String suffix, String type) {

		CodeGenerate query = DTOUtils.newDTO(CodeGenerate.class);
		query.setType(type);
		query.setSuffix(suffix);
		query.setCreated(new Date());
		query.setModified(new Date());
		query.setCode(0L);
		return 0;

	}

	private Optional<CodeGenerate> selectCodeGenerateByType(String type) {
		CodeGenerate query = DTOUtils.newDTO(CodeGenerate.class);
		query.setType(SAMPLE_CODE_TYPE);
		query.setOrder("id");
		query.setSort("asc");
		return this.listByExample(query).stream().findFirst();
	}

}
