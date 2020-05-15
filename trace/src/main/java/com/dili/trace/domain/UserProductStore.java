package com.dili.trace.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户库存商品信息
 */
@Table(name = "`user_product_store`")
public class UserProductStore extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`parent_id`")
    private Long parentId;

    
    @Column(name = "`bill_id`")
    private Long billId;
    @Column(name = "`seperate_record_id`")
    private Long seperateRecordId;
    /**
     * 类型 {@link com.dili.trace.glossary.UserProductStoreTypeEnum}
     */
    @ApiModelProperty(value = "类型")
    @Column(name = "`user_product_store_type`")
    private Integer userProductStoreType;


    @ApiModelProperty(value = "业户ID")
    @Column(name = "`user_id`")
    private Long userId;

    @ApiModelProperty(value = "业户姓名")
    @Column(name = "`user_name`")
    private String userName;

    
    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    private String productName;


    @ApiModelProperty(value = "重量")
    @Column(name = "`weight`")
    private BigDecimal weight;

    @ApiModelProperty(value = "未分销重量")
    @Column(name = "`store_weight`")
    private BigDecimal storeWeight;


    @ApiModelProperty(value = "创建时间")
    @Column(name = "`operator_id`")
    private Date created;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "`operator_id`")
    private Date modified;

   

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Long return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return Integer return the userProductStoreType
     */
    public Integer getUserProductStoreType() {
        return userProductStoreType;
    }

    /**
     * @param userProductStoreType the userProductStoreType to set
     */
    public void setUserProductStoreType(Integer userProductStoreType) {
        this.userProductStoreType = userProductStoreType;
    }

    /**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return String return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return String return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }


    /**
     * @return Date return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return Date return the modified
     */
    public Date getModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }


    /**
     * @return BigDecimal return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return BigDecimal return the storeWeight
     */
    public BigDecimal getStoreWeight() {
        return storeWeight;
    }

    /**
     * @param storeWeight the storeWeight to set
     */
    public void setStoreWeight(BigDecimal storeWeight) {
        this.storeWeight = storeWeight;
    }


    /**
     * @return Long return the billId
     */
    public Long getBillId() {
        return billId;
    }

    /**
     * @param billId the billId to set
     */
    public void setBillId(Long billId) {
        this.billId = billId;
    }


    /**
     * @return Long return the seperateRecordId
     */
    public Long getSeperateRecordId() {
        return seperateRecordId;
    }

    /**
     * @param seperateRecordId the seperateRecordId to set
     */
    public void setSeperateRecordId(Long seperateRecordId) {
        this.seperateRecordId = seperateRecordId;
    }

}