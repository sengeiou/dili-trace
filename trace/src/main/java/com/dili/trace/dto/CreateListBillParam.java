package com.dili.trace.dto;

import java.util.List;

import com.dili.trace.api.input.CreateRegisterBillInputDto;

public class CreateListBillParam {
	private Long userId;
	private Long marketId;
	private Integer registerSource;
	private String tallyAreaNo;
	private Integer tradeTypeId;
	private String plate;
	private String tradeAccount;

	private String tradePrintingCard;
	private String name;
	private String idCardNo;
	private String addr;
	private String phone;


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

	public Integer getRegisterSource() {
		return registerSource;
	}

	public void setRegisterSource(Integer registerSource) {
		this.registerSource = registerSource;
	}

	public String getTallyAreaNo() {
		return tallyAreaNo;
	}

	public void setTallyAreaNo(String tallyAreaNo) {
		this.tallyAreaNo = tallyAreaNo;
	}

	public Integer getTradeTypeId() {
		return tradeTypeId;
	}

	public void setTradeTypeId(Integer tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getTradeAccount() {
		return tradeAccount;
	}

	public void setTradeAccount(String tradeAccount) {
		this.tradeAccount = tradeAccount;
	}

	public String getTradePrintingCard() {
		return tradePrintingCard;
	}

	public void setTradePrintingCard(String tradePrintingCard) {
		this.tradePrintingCard = tradePrintingCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
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
}
