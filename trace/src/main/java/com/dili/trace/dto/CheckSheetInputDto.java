package com.dili.trace.dto;

import java.util.List;

import javax.persistence.Transient;

import com.dili.trace.domain.CheckSheet;

public interface CheckSheetInputDto extends CheckSheet {

	@Transient
	public List<RegisterBillDto> getRegisterBillList();

	public void setRegisterBillList(List<RegisterBillDto> registerBillList);

}
