package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.CheckSheet;
import com.dili.trace.dto.CheckSheetInputDto;
import com.dili.trace.dto.CheckSheetPrintDto;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface CheckSheetService extends BaseService<CheckSheet, Long> {
	/**
	 * 创建CheckSheet
	 * @param input
	 * @return
	 */
	public CheckSheet createCheckSheet(CheckSheetInputDto input);
	/**
	 * 打印预览CheckSheet
	 * @param input
	 * @return
	 */
	public CheckSheetPrintDto prePrint(CheckSheetInputDto input);

}