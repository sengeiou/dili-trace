package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.exception.AppException;
import com.dili.trace.domain.CodeGenerate;

public interface CodeGenerateService extends BaseService<CodeGenerate, Long>{
	public String nextSampleCode();
}
