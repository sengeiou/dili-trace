package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

@Table(name = "thirty_party_push_data")
public class ThirtyPartyPushData extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "interface_name")
    private String interfaceName;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "table_id")
    private Integer tableId;

    @Column(name = "push_time")
    private Date pushTime;

    @Column(name = "created")
    private Date created;

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

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
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
}
