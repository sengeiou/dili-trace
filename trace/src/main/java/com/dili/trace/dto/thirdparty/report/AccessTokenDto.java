package com.dili.trace.dto.thirdparty.report;

public class AccessTokenDto implements ReportDto {
    private String appId;// 应用ID
    private String appSecret;// 应用密钥

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return the appSecret
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * @param appSecret the appSecret to set
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }


}