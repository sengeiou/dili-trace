package com.dili.trace.api.output;

public class VerifyBillInputDto {
    /**
     * 报备单id
     */
    private Long billId;
    /**
     * 验证状态
     */
    private Integer verifyStatus;
    /**
     * 原因
     */
    private String reason;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }


    /**
     * @return String return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

}
