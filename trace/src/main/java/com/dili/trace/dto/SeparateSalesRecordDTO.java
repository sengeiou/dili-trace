package com.dili.trace.dto;

import javax.persistence.Table;

import com.dili.trace.domain.SeparateSalesRecord;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`separate_sales_record`")
public interface SeparateSalesRecordDTO extends SeparateSalesRecord {
	
	   
    String getTradeNo();

    void setTradeNo(String tradeNo);
	
   
    Integer getRegisterSource();

    void setRegisterSource(Integer registerSource);


}