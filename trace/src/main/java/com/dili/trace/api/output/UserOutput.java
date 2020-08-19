package com.dili.trace.api.output;

public class UserOutput {
	private Long id;
	private Integer validateState;
	private String phone;
	private String tallyAreaNos;
	private String name;
	private String numbers;
	private Integer qrStatus;
	private Integer cnt;
	private String marketName;
	private String businessLicenseUrl;

	public String getBusinessLicenseUrl() {
		return businessLicenseUrl;
	}

	public void setBusinessLicenseUrl(String businessLicenseUrl) {
		this.businessLicenseUrl = businessLicenseUrl;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public Integer getQrStatus() {
		return qrStatus;
	}

	public void setQrStatus(Integer qrStatus) {
		this.qrStatus = qrStatus;
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
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

    /**
     * @return Integer return the cnt
     */
    public Integer getCnt() {
        return cnt;
    }

    /**
     * @param cnt the cnt to set
     */
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

}
