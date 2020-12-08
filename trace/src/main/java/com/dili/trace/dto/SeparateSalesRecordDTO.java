package com.dili.trace.dto;

import com.dili.trace.domain.SeparateSalesRecord;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`separate_sales_record`")
public class SeparateSalesRecordDTO extends SeparateSalesRecord {

	/**
	 * 交易单号
	 */
	@Transient
	private String tradeNo;
	/**
	 * 注册来源
	 */
	@Transient
	private Integer registerSource;

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Integer getRegisterSource() {
		return registerSource;
	}

	public void setRegisterSource(Integer registerSource) {
		this.registerSource = registerSource;
	}

}