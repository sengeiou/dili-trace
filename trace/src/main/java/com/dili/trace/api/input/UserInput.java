package com.dili.trace.api.input;

import com.dili.ss.domain.BasePage;

public class UserInput extends BasePage {
	private Long id;
	private Integer validateState;
	private String tallyAreaNos;
	private String name;
	private String refuseReason;

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getValidateState() {
		return validateState;
	}

	public void setValidateState(Integer validateState) {
		this.validateState = validateState;
	}

	public String getTallyAreaNos() {
		return tallyAreaNos;
	}

	public void setTallyAreaNos(String tallyAreaNos) {
		this.tallyAreaNos = tallyAreaNos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
