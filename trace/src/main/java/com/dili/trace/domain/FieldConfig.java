package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "field_config")
public class FieldConfig extends BaseDomain {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    /**
     * 模块
     */
    @Column(name = "module_type")
    private Integer moduleType;

    /**
     * 从哪个修改而来(保留修改历史)
     */
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 市场id
     */
    @Column(name = "market_id")
    private Long marketId;

    /**
     * 是否有效
     */
    @Column(name = "is_valid")
    private Integer isValid;

    /**
     * 操作人ID
     */
    @Column(name = "operator_id")
    private Long operatorId;
    /**
     * 操作人姓名
     */
    @Column(name = "operator_name")
    private String operatorName;

    /**
     * 创建时间
     */
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * 更新时间
     */
    @Column(name = "modified")
    private LocalDateTime modified;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }


    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getModuleType() {
        return moduleType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }
}
