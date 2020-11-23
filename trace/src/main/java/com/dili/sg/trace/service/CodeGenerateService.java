package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.CodeGenerate;
import com.dili.trace.glossary.BillTypeEnum;

public interface CodeGenerateService extends BaseService<CodeGenerate, Long> {
	public void init();
	
	
	
	public String nextRegisterBillCode();
	/**
	 * 生成采样单编号
	 * 
	 * @return
	 */
	public String nextRegisterBillSampleCode();
		/**
	 * 生成委托单编号
	 * 
	 * @return
	 */
	public String nextCommissionBillCode();

		/**
	 * 生成委托单采样单编号
	 * 
	 * @return
	 */
	public String nextCommissionBillSampleCode();

	/**
	 * 生成打印报告编号
	 * 
	 * @return
	 */

	public String nextCheckSheetCode(BillTypeEnum taskTypeEnum);
	
	public String getMaskCheckSheetCode(BillTypeEnum taskTypeEnum);
	
	/**
 * 生成委托单编号
 * 
 * @return
 */
public String nextECommerceBillCode();
	
	/**
 * 生成委托单采样单编号
 * 
 * @return
 */
public String nextECommerceBillSampleCode();
}
