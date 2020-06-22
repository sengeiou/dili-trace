package com.dili.trace.dto;

import java.util.List;

import com.dili.trace.api.input.CreateRegisterBillInputDto;

public class CreateListBillParam {
	private List<CreateRegisterBillInputDto> registerBills;

	public List<CreateRegisterBillInputDto> getRegisterBills() {
		return registerBills;
	}

	public void setRegisterBills(List<CreateRegisterBillInputDto> registerBills) {
		this.registerBills = registerBills;
	}
}
