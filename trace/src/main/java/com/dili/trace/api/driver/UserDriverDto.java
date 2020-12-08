package com.dili.trace.api.driver;

import java.util.Date;

/**
 * @version 1.0
 * @ClassName
 * @Description todo
 * @createTime 2020年12月01日 17:34:00
 */
public class UserDriverDto {
    /**
     * ID
     */
    private Long id;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 卖家ID
     */
    private Long sellerId;

    /**
     * 卖家名称
     */
    private String sellerName;

    /**
     * 卖家分享时间
     */
    private Date shareTime;

    /**
     * 卖家摊位号
     */
    private String tallyAreaNos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Date getShareTime() {
        return shareTime;
    }

    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
    }

    public String getTallyAreaNos() {
        return tallyAreaNos;
    }

    public void setTallyAreaNos(String tallyAreaNos) {
        this.tallyAreaNos = tallyAreaNos;
    }
}
