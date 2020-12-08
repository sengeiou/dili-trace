package com.dili.trace.api.output;

public class UserQrOutput {
    /**
     * 业户id
     */
    private Long userId;
    /**
     * 更新时间
     */
    private String updated;
    /**
     * 64位图片码
     */
    private String base64QRImg;
    /**
     * 业户名称
     */
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return String return the updated
     */
    public String getUpdated() {
        return updated;
    }

    /**
     * @param updated the updated to set
     */
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    /**
     * @return String return the base64QRImg
     */
    public String getBase64QRImg() {
        return base64QRImg;
    }

    /**
     * @param base64QRImg the base64QRImg to set
     */
    public void setBase64QRImg(String base64QRImg) {
        this.base64QRImg = base64QRImg;
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