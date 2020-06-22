package com.dili.trace.api.output;

public class CheckoutApiListQuery {

    private Long separateSalesId;
    private Long userId;
    private String likeProductName;
    private String date;

    public String getLikeProductName() {
        return likeProductName;
    }

    public void setLikeProductName(String likeProductName) {
        this.likeProductName = likeProductName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getSeparateSalesId() {
        return separateSalesId;
    }

    public void setSeparateSalesId(Long separateSalesId) {
        this.separateSalesId = separateSalesId;
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
