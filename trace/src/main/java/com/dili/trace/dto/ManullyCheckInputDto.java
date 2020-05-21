package com.dili.trace.dto;

public class ManullyCheckInputDto {
    private Long billId;
    private Boolean pass;
    

    /**
     * @return Long return the billId
     */
    public Long getBillId() {
        return billId;
    }

    /**
     * @param billId the billId to set
     */
    public void setBillId(Long billId) {
        this.billId = billId;
    }




    /**
     * @return Boolean return the pass
     */
    public Boolean getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(Boolean pass) {
        this.pass = pass;
    }

}