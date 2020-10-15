package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

@Table(name = "third_party_push_data")
public class ThirdPartyPushData extends BaseDomain {

    public ThirdPartyPushData() {}
    public ThirdPartyPushData(String interfaceName, String tableName, Long marketId) {
        this.interfaceName = interfaceName;
        this.tableName = tableName;
        this.marketId = marketId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "interface_name")
    private String interfaceName;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "push_time")
    private Date pushTime;

    @Column(name = "created")
    private Date created;

    @Column(name = "market_id")
    private Long marketId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
}
