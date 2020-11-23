package com.dili.sg.trace.dto;

import com.dili.sg.trace.util.MaskUserInfo;

public class UserInfoDto {
	private String userId;
	private String name;
	private String addr;
	private String phone;
	private String idCardNo;
	private String printingCard;

	public String getPrintingCard() {
		return printingCard;
	}

	public void setPrintingCard(String printingCard) {
		this.printingCard = printingCard;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserInfoDto mask(boolean mask) {
		if (mask) {

			this.setIdCardNo(MaskUserInfo.maskIdNo(this.getIdCardNo()));
			this.setAddr(MaskUserInfo.maskAddr(this.getAddr()));
		}
		return this;
	}

}
