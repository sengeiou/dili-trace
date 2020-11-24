package com.dili.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 默认配置信息
 */
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

    /**
     *getEnTag
     */
    public String getEnTag() {
        return enTag;
    }

    /**
     *
     * @param enTag
     */
    public void setEnTag(String enTag) {
        this.enTag = enTag;
    }

    /**
     *getSessionExpire
     */

    public long getSessionExpire() {
        return sessionExpire;
    }

    /**
     *setSessionExpire
     */
    public void setSessionExpire(long sessionExpire) {
        this.sessionExpire = sessionExpire;
    }

    /**
     *getMarketId
     */
    public long getMarketId() {
        return marketId;
    }

    /**
     *setMarketId
     */
    public void setMarketId(long marketId) {
        this.marketId = marketId;
    }

    /**
     *getCheckCodeExpire
     */
    public long getCheckCodeExpire() {
        return checkCodeExpire;
    }

    /**
     *setCheckCodeExpire
     */
    public void setCheckCodeExpire(long checkCodeExpire) {
        this.checkCodeExpire = checkCodeExpire;
    }

    /**
     *getCheckCodeLength
     */
    public int getCheckCodeLength() {
        return checkCodeLength;
    }

    /**
     *setCheckCodeLength
     */
    public void setCheckCodeLength(int checkCodeLength) {
        this.checkCodeLength = checkCodeLength;
    }

    /**
     *getImageDirectory
     */
    public String getImageDirectory() {
        return imageDirectory;
    }

    /**
     *setImageDirectory
     */
    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    /**
     *getImageScale
     */
    public double getImageScale() {
        return imageScale;
    }

    /**
     *setImageScale
     */
    public void setImageScale(double imageScale) {
        this.imageScale = imageScale;
    }

    /**
     *getImageQuality
     */
    public double getImageQuality() {
        return imageQuality;
    }

    /**
     *setImageQuality
     */
    public void setImageQuality(double imageQuality) {
        this.imageQuality = imageQuality;
    }


    /**
     *getPassword
     */
    public String getPassword() {
        return password;
    }

    /**
     *setPassword
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
