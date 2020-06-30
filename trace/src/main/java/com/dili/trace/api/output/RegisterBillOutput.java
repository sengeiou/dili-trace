package com.dili.trace.api.output;

import com.dili.trace.domain.RegisterBill;
import com.dili.trace.enums.BillVerifyStatusEnum;

public class RegisterBillOutput {
	private Long billId;
	private Long userId;
	private Integer verifyStatus;
	private String verifyStatusDesc;
	private String productName;
	private Integer color;
	public static RegisterBillOutput build(RegisterBill bill){
		RegisterBillOutput out=new RegisterBillOutput();
		out.setBillId(bill.getId());
		out.setVerifyStatus(bill.getVerifyStatus());
		out.setProductName(bill.getProductName());
		out.setVerifyStatusDesc(BillVerifyStatusEnum.fromCode(bill.getVerifyStatus())
								.map(BillVerifyStatusEnum::getName).orElse(""));
		out.setUserId(bill.getUserId());
		return out;
	}
	
	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyStatusDesc() {
		return verifyStatusDesc;
	}

	public void setVerifyStatusDesc(String verifyStatusDesc) {
		this.verifyStatusDesc = verifyStatusDesc;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}
	
	
	


    /**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
