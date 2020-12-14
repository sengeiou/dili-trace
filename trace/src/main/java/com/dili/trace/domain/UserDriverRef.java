package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author asa.lee
 */
@Table(name = "`user_driver_ref`")
public class UserDriverRef extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "share_time")
    private Date shareTime;

    @Column(name = "tally_area_nos")
    private String tallyAreaNos;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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
