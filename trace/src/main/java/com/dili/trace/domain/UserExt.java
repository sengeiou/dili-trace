package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alvin.Li
 */
@Table(name = "`user_ext`")
public class UserExt extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    /**
     * 经营户主键 {@link com.dili.trace.domain.User}
     */
    @ApiModelProperty(value = "经营户主键")
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 证件类型
     */
    @ApiModelProperty(value = "证件类型")
    @Column(name = "`credential_type`")
    private String credentialType;

    /**
     * 证件名称
     */
    @ApiModelProperty(value = "证件名称")
    @Column(name = "`credential_name`")
    private String credentialName;

    /**
     * 证件号码
     */
    @ApiModelProperty(value = "证件号码")
    @Column(name = "`credential_number`")
    private String credentialNumber;

    /**
     * 证件url
     */
    @ApiModelProperty(value = "证件url")
    @Column(name = "`credential_url`")
    private String credentialUrl;

    /**
     * 身份证地址
     */
    @ApiModelProperty(value = "身份证地址")
    @Column(name = "`id_addr`")
    private String idAddr;

    /**
     * 商品去向
     */
    @ApiModelProperty(value = "商品去向")
    @Column(name = "`whereis`")
    private String whereis;

    /**
     * 授信额度
     */
    @ApiModelProperty(value = "授信额度")
    @Column(name = "`credit_limit`")
    private String creditLimit;

    /**
     * 卡有效期
     */
    @ApiModelProperty(value = "卡有效期")
    @Column(name = "`effective_date`")
    private Date effectiveDate;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Column(name = "`remark`")
    private String remark;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    @Column(name = "`sex`")
    private String sex;

    /**
     * 固定电话
     */
    @ApiModelProperty(value = "固定电话")
    @Column(name = "`fixed_telephone`")
    private String fixedTelephone;

    /**
     * 手续费折扣率
     */
    @ApiModelProperty(value = "手续费折扣率")
    @Column(name = "`charge_rate`")
    private BigDecimal chargeRate;

    /**
     * 包装管理费折扣率
     */
    @ApiModelProperty(value = "包装管理费折扣率")
    @Column(name = "`manger_rate`")
    private BigDecimal mangerRate;

    /**
     * 仓储费折扣率
     */
    @ApiModelProperty(value = "仓储费折扣率")
    @Column(name = "`storage_rate`")
    private BigDecimal storageRate;

    /**
     * 员工考核折扣率
     */
    @ApiModelProperty(value = "员工考核折扣率")
    @Column(name = "`assess_rate`")
    private BigDecimal assessRate;


    /**
     * 折扣率批准人
     */
    @ApiModelProperty(value = "折扣率批准人")
    @Column(name = "`approver`")
    private String approver;

    /**
     * 供应商类型（大客户、临时客户）
     */
    @ApiModelProperty(value = "供应商类型（大客户、临时客户）")
    @Column(name = "`supplier_type`")
    private String supplierType;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialName() {
        return credentialName;
    }

    public void setCredentialName(String credentialName) {
        this.credentialName = credentialName;
    }

    public String getCredentialNumber() {
        return credentialNumber;
    }

    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }

    public String getIdAddr() {
        return idAddr;
    }

    public void setIdAddr(String idAddr) {
        this.idAddr = idAddr;
    }

    public String getWhereis() {
        return whereis;
    }

    public void setWhereis(String whereis) {
        this.whereis = whereis;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFixedTelephone() {
        return fixedTelephone;
    }

    public void setFixedTelephone(String fixedTelephone) {
        this.fixedTelephone = fixedTelephone;
    }

    public BigDecimal getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(BigDecimal chargeRate) {
        this.chargeRate = chargeRate;
    }

    public BigDecimal getMangerRate() {
        return mangerRate;
    }

    public void setMangerRate(BigDecimal mangerRate) {
        this.mangerRate = mangerRate;
    }

    public BigDecimal getStorageRate() {
        return storageRate;
    }

    public void setStorageRate(BigDecimal storageRate) {
        this.storageRate = storageRate;
    }

    public BigDecimal getAssessRate() {
        return assessRate;
    }

    public void setAssessRate(BigDecimal assessRate) {
        this.assessRate = assessRate;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }
}
