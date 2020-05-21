package com.dili.trace.api.dto;

import com.dili.trace.glossary.RegisterBillStateEnum;

public class CheckInApiDetailOutput {
	private Long id;
	private Integer state;
	private String code;
	private String name;
	private String phone;
	private String upstreamName;
	private String upstreamTelphone;

	public String getStateName() {
		try {
			if (getState() == null) {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
		return RegisterBillStateEnum.getRegisterBillStateEnum(getState()).getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUpstreamName() {
		return upstreamName;
	}

	public void setUpstreamName(String upstreamName) {
		this.upstreamName = upstreamName;
	}

	public String getUpstreamTelphone() {
		return upstreamTelphone;
	}

	public void setUpstreamTelphone(String upstreamTelphone) {
		this.upstreamTelphone = upstreamTelphone;
	}



}
