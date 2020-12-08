package com.dili.trace.api.input;

public class RegisterBillApiInputDto {
    /**
     * 报备单id
     */
	private Long billId;
    /**
     * 交易单明细id
     */
	private Long tradeDetailId;
	

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}


    /**
     * @return Long return the tradeDetailId
     */
    public Long getTradeDetailId() {
        return tradeDetailId;
    }

    /**
     * @param tradeDetailId the tradeDetailId to set
     */
    public void setTradeDetailId(Long tradeDetailId) {
        this.tradeDetailId = tradeDetailId;
    }

}
