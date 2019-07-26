package com.dili.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "default")
public class DefaultConfiguration {

    //session过期时间 单位秒
    private long sessionExpire;
    //默认市场id
    private long marketId;
    //默认手机验证码失效时间 单位秒
    private long checkCodeExpire;
    //手机验证码默认长度
    private int checkCodeLength;
    //图片保存目录
    private String imageDirectory;
    //图片scale
    private double imageScale;
    //图片质量
    private double imageQuality;
    //签名key
    private String signSalt;
    //消息域名
    private String messageHost;
    //系统简称
    private String systemCode;
    //货站默认密码
    private String stationPassword;

    public long getSessionExpire() {
        return sessionExpire;
    }

    public void setSessionExpire(long sessionExpire) {
        this.sessionExpire = sessionExpire;
    }

    public long getMarketId() {
        return marketId;
    }

    public void setMarketId(long marketId) {
        this.marketId = marketId;
    }

    public long getCheckCodeExpire() {
        return checkCodeExpire;
    }

    public void setCheckCodeExpire(long checkCodeExpire) {
        this.checkCodeExpire = checkCodeExpire;
    }

    public int getCheckCodeLength() {
        return checkCodeLength;
    }

    public void setCheckCodeLength(int checkCodeLength) {
        this.checkCodeLength = checkCodeLength;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    public double getImageScale() {
        return imageScale;
    }

    public void setImageScale(double imageScale) {
        this.imageScale = imageScale;
    }

    public double getImageQuality() {
        return imageQuality;
    }

    public void setImageQuality(double imageQuality) {
        this.imageQuality = imageQuality;
    }

    public String getSignSalt() {
        return signSalt;
    }

    public void setSignSalt(String signSalt) {
        this.signSalt = signSalt;
    }

    public String getMessageHost() {
        return messageHost;
    }

    public void setMessageHost(String messageHost) {
        this.messageHost = messageHost;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getStationPassword() {
        return stationPassword;
    }

    public void setStationPassword(String stationPassword) {
        this.stationPassword = stationPassword;
    }
}
