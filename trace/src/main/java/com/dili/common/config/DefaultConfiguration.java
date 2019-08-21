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
    //货站默认密码
    private String password;
    private String enTag;

    public String getEnTag() {
        return enTag;
    }

    public void setEnTag(String enTag) {
        this.enTag = enTag;
    }

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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
