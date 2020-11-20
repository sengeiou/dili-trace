package com.dili.trace.api.input;

/**
 * @author asa.lee
 */
public class RegisterBillQueryInputDto {
    /**
     * 报备单 code
     */
	private String billId;
    /**
     * 经营户卡号
     */
	private String supplierId;
    /**
     * 经营户名称
     */
    private String supplierName;
    /**
     * 商品码
     */
    private String categoryCode;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

}
