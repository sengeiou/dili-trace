package com.dili.trace.service;

import java.util.Map;
import java.util.Optional;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.CheckSheet;
import com.dili.trace.dto.CheckSheetInputDto;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface CheckSheetService extends BaseService<CheckSheet, Long> {
	/**
	 * 通过code查询CheckSheet
	 * @param code
	 * @return
	 */
	public  Optional<CheckSheet> findCheckSheetByCode(String code);
	/**
	 * 创建CheckSheet
	 * @param input
	 * @return
	 */
	public Map createCheckSheet(CheckSheetInputDto input);
	/**
	 * 打印预览CheckSheet
	 * @param input
	 * @return
	 */
	public Map prePrint(CheckSheetInputDto input);
	
	/**
	 * 生成base64二维码
	 * @param content
	 * @param qrWidth
	 * @param qrHeight
	 * @return
	 */
	public  String getBase64(String content, int qrWidth, int qrHeight);
	
	/**
	 * 通过ID查询可调用打印接口的数据结构
	 * @param id
	 * @return
	 */
	public Map findPrintableCheckSheet(Long id);

}