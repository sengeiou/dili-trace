package com.dili.trace.excel;

import com.alibaba.excel.annotation.ExcelProperty;

// import com.alibaba.excel.annotation.ExcelProperty;

public class IndexOrNameData {

    /**
     * value: 表头名称 index: 列的号, 0表示第一列
     */
    @ExcelProperty(value = "姓名", index = 0)
    private String orderNum;
    /**
     * value: 表头名称 index: 列的号, 0表示第一列
     */
    @ExcelProperty(value = "姓名", index = 1)
    private String name;

     @ExcelProperty(value = "手机号", index = 2)
    private String phone;

     @ExcelProperty(value = "身份证号", index = 3)
     private  String cardNo;
     @ExcelProperty(value = "统一信用代码", index = 4)
    private String license;

     @ExcelProperty(value = "法人姓名", index = 5)
    private String legalPerson;
     
     @ExcelProperty(value = "用户类型", index = 6)
    private String vocationTypeName;
     
     @ExcelProperty(value = "用户类型", index = 7)
    private String marketName;
     @ExcelProperty(value = "营业执照", index = 8)
     private String businessLicenseUrl;
     
     @ExcelProperty(value = "摊位号", index = 9)
     private String tallyAreaNo;
     
     @ExcelProperty(value = "品类类型", index = 10)
     private String preserveTypeName;
     
     @ExcelProperty(value = "产品名称", index = 11)
     private String categoryName;
    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getVocationTypeName() {
		return vocationTypeName;
	}

	public void setVocationTypeName(String vocationTypeName) {
		this.vocationTypeName = vocationTypeName;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getBusinessLicenseUrl() {
		return businessLicenseUrl;
	}

	public void setBusinessLicenseUrl(String businessLicenseUrl) {
		this.businessLicenseUrl = businessLicenseUrl;
	}

	public String getTallyAreaNo() {
		return tallyAreaNo;
	}

	public void setTallyAreaNo(String tallyAreaNo) {
		this.tallyAreaNo = tallyAreaNo;
	}

	public String getPreserveTypeName() {
		return preserveTypeName;
	}

	public void setPreserveTypeName(String preserveTypeName) {
		this.preserveTypeName = preserveTypeName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

    



}
