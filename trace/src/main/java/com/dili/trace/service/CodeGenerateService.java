package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.CodeGenerate;

public interface CodeGenerateService extends BaseService<CodeGenerate, Long> {
	/**
	 * 生成采样单编号
	 * 
	 * @return
	 */
	public String nextTradeRequestCode();

}
