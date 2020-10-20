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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	private List<CreateRegisterHeadInputDto> registerHeads;

	public List<CreateRegisterHeadInputDto> getRegisterHeads() {
		return registerHeads;
	}

	public void setRegisterHeads(List<CreateRegisterHeadInputDto> registerHeads) {
		this.registerHeads = registerHeads;
	}
}
