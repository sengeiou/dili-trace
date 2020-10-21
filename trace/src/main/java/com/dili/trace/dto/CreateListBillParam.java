package com.dili.trace.dto;

import java.util.List;

import com.dili.trace.api.input.CreateRegisterBillInputDto;

public class CreateListBillParam {
	private Long userId;
	private Long marketId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	private List<CreateRegisterBillInputDto> registerBills;

	public List<CreateRegisterBillInputDto> getRegisterBills() {
		return registerBills;
	}

	public void setRegisterBills(List<CreateRegisterBillInputDto> registerBills) {
		this.registerBills = registerBills;
	}

	public Long getMarketId() {
		return marketId;
	}

	public void setMarketId(Long marketId) {
		this.marketId = marketId;
	}
}
