package com.dili.trace.dto;

import com.dili.ss.domain.BaseDomain;

public class BillReportQueryDto extends BaseDomain {
    private String billCreatedStart;

    private String billCreatedEnd;

    private String checkinCreatedStart;

    private String checkinCreatedEnd;

    private Boolean includeUnCheckin;
    private String likeCode;

    private String likeUserName;
    
    private String likeProductName;

    private Integer verifyStatus;

    private Long originId;
    
    private Long productId;

    private String likePlate;
    
    private String dynamicSql;
    
    

    

    /**
     * @return String return the billCreatedStart
     */
    public String getBillCreatedStart() {
        return billCreatedStart;
    }

    /**
     * @param billCreatedStart the billCreatedStart to set
     */
    public void setBillCreatedStart(String billCreatedStart) {
        this.billCreatedStart = billCreatedStart;
    }

    /**
     * @return String return the billCreatedEnd
     */
    public String getBillCreatedEnd() {
        return billCreatedEnd;
    }

    /**
     * @param billCreatedEnd the billCreatedEnd to set
     */
    public void setBillCreatedEnd(String billCreatedEnd) {
        this.billCreatedEnd = billCreatedEnd;
    }

    /**
     * @return String return the checkinCreatedStart
     */
    public String getCheckinCreatedStart() {
        return checkinCreatedStart;
    }

    /**
     * @param checkinCreatedStart the checkinCreatedStart to set
     */
    public void setCheckinCreatedStart(String checkinCreatedStart) {
        this.checkinCreatedStart = checkinCreatedStart;
    }

    /**
     * @return String return the checkinCreatedEnd
     */
    public String getCheckinCreatedEnd() {
        return checkinCreatedEnd;
    }

    /**
     * @param checkinCreatedEnd the checkinCreatedEnd to set
     */
    public void setCheckinCreatedEnd(String checkinCreatedEnd) {
        this.checkinCreatedEnd = checkinCreatedEnd;
    }



    /**
     * @param includeUnCheckin the includeUnCheckin to set
     */
    public void setIncludeUnCheckin(Boolean includeUnCheckin) {
        this.includeUnCheckin = includeUnCheckin;
    }



    /**
     * @return String return the likeUserName
     */
    public String getLikeUserName() {
        return likeUserName;
    }

    /**
     * @param likeUserName the likeUserName to set
     */
    public void setLikeUserName(String likeUserName) {
        this.likeUserName = likeUserName;
    }

    /**
     * @return String return the likeProductName
     */
    public String getLikeProductName() {
        return likeProductName;
    }

    /**
     * @param likeProductName the likeProductName to set
     */
    public void setLikeProductName(String likeProductName) {
        this.likeProductName = likeProductName;
    }


    /**
     * @return Boolean return the includeUnCheckin
     */
    public Boolean isIncludeUnCheckin() {
        return includeUnCheckin;
    }

    /**
     * @return String return the dynamicSql
     */
    public String getDynamicSql() {
        return dynamicSql;
    }

    /**
     * @param dynamicSql the dynamicSql to set
     */
    public void setDynamicSql(String dynamicSql) {
        this.dynamicSql = dynamicSql;
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
     * @return Long return the originId
     */
    public Long getOriginId() {
        return originId;
    }

    /**
     * @param originId the originId to set
     */
    public void setOriginId(Long originId) {
        this.originId = originId;
    }


    /**
     * @return Long return the productId
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }


    /**
     * @return String return the likePlate
     */
    public String getLikePlate() {
        return likePlate;
    }

    /**
     * @param likePlate the likePlate to set
     */
    public void setLikePlate(String likePlate) {
        this.likePlate = likePlate;
    }


    /**
     * @return String return the likeCode
     */
    public String getLikeCode() {
        return likeCode;
    }

    /**
     * @param likeCode the likeCode to set
     */
    public void setLikeCode(String likeCode) {
        this.likeCode = likeCode;
    }

}