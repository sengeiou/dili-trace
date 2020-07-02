package com.dili.trace.api.output;

import java.util.Objects;

import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.VerifyTypeEnum;

public class VerifyStatusCountOutputDto {
    private Integer verifyStatus;
    private String verifyStatusName;
    private Integer verifyType;
    private String verifyTypeName;
    private Integer num;

    public static VerifyStatusCountOutputDto buildDefault(VerifyTypeEnum verifyType,
            BillVerifyStatusEnum verifyStatus) {
        VerifyStatusCountOutputDto dto = new VerifyStatusCountOutputDto();
        dto.setNum(0);
        dto.setVerifyType(verifyType.getCode());
        dto.setVerifyStatusName(verifyType.getDesc());
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
     * @return Integer return the verifyType
     */
    public Integer getVerifyType() {
        return verifyType;
    }

    /**
     * @param verifyType the verifyType to set
     */
    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    /**
     * @return String return the verifyTypeName
     */
    public String getVerifyTypeName() {
        return verifyTypeName;
    }

    /**
     * @param verifyTypeName the verifyTypeName to set
     */
    public void setVerifyTypeName(String verifyTypeName) {
        this.verifyTypeName = verifyTypeName;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(verifyStatus,verifyType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VerifyStatusCountOutputDto)) {
            return false;
        }
        VerifyStatusCountOutputDto other = (VerifyStatusCountOutputDto) obj;
        return Objects.equals(verifyStatus, other.verifyStatus)
                && Objects.equals(verifyType, other.verifyType) ;
    }

}