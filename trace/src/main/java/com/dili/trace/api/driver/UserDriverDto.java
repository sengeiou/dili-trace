package com.dili.trace.api.driver;

import java.util.Date;

/**
 * @version 1.0
 * @ClassName
 * @Description todo
 * @createTime 2020年12月01日 17:34:00
 */
public class UserDriverDto {
    private Long id;

    private Long driverId;

    private String driverName;

    private Long sellerId;

    private String sellerName;

    private Date shareTime;

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
