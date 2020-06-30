package com.dili.trace.api.output;

public class UserOutput {
	private Long id;
	private Integer validateState;
	private String phone;
	private String tallyAreaNos;
	private String name;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
