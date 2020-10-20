package com.dili.trace.dto;

import com.dili.trace.api.input.CreateRegisterHeadInputDto;

import java.util.List;

/**
 * 进门主台账单参数接收类
 *
 * @author Lily
 */
public class CreateListRegisterHeadParam {
	private Long userId;
	private Long marketId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	private List<CreateRegisterHeadInputDto> registerBills;

	public List<CreateRegisterHeadInputDto> getRegisterBills() {
		return registerBills;
	}

	public void setRegisterBills(List<CreateRegisterHeadInputDto> registerBills) {
		this.registerBills = registerBills;
	}

	public Long getMarketId() {
		return marketId;
	}

	public void setMarketId(Long marketId) {
		this.marketId = marketId;
	}
}
