package com.dili.trace.api.output;

import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.VerifyTypeEnum;

public class VerifyStatusCountOutputDto {
    /**
     * 查验状态
     */
    private Integer verifyStatus;

    /**
     * 状态名称
     */
    private String verifyStatusName;

    /**
     * 数量
     */
    private Integer num;

    public VerifyStatusCountOutputDto() {
    }

    public VerifyStatusCountOutputDto(Integer verifyStatus, Integer num) {
        this.verifyStatus = verifyStatus;
        this.num = num;
    }

    public static VerifyStatusCountOutputDto buildDefault(
            BillVerifyStatusEnum verifyStatus) {
        VerifyStatusCountOutputDto dto = new VerifyStatusCountOutputDto();
        dto.setNum(0);
        
        dto.setVerifyStatus(verifyStatus.getCode());
        dto.setVerifyStatusName(verifyStatus.getName());
        return dto;
    }

    /**
     * @return Integer return the verifyStatus
     */
    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    /**
     * @param verifyStatus the verifyStatus to set
     */
    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    /**
     * @return String return the verifyStatusName
     */
    public String getVerifyStatusName() {
        return verifyStatusName;
    }

    /**
     * @param verifyStatusName the verifyStatusName to set
     */
    public void setVerifyStatusName(String verifyStatusName) {
        this.verifyStatusName = verifyStatusName;
    }

    /**
     * @return Integer return the num
     */
    public Integer getNum() {
        return num;
    }

    /**
     * @param num the num to set
     */
    public void setNum(Integer num) {
        this.num = num;
    }

}