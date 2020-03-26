package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.CheckSheet;
import com.dili.trace.dto.CheckSheetInputDto;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface CheckSheetService extends BaseService<CheckSheet, Long> {
	public CheckSheet createCheckSheet(CheckSheetInputDto input);

}