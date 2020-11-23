package com.dili.sg.trace.domain;

import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 *
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`check_sheet_detail`")
public class CheckSheetDetail extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`check_sheet_id`")
    private Long checkSheetId;


    @Column(name = "`register_bill_id`")
    private Long registerBillId;


    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    private String productName;



    @ApiModelProperty(value = "产地名")
    @Column(name = "`origin_name`")
    private String originName;



    @ApiModelProperty(value = "1.合格 2.不合格 3.复检合格 4.复检不合格")
    @Column(name = "`detect_state`")

    private Integer detectState;


    @Column(name = "`product_alias_name`")
    public String productAliasName;


    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    Date created;



    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
    Date modified;

    @Column(name = "`order_number`")
    Integer orderNumber;

    @Transient
    private String detectStateView;

    @ApiModelProperty(value = "检测结果")
    @Column(name = "`latest_pd_result`")
    private String latestPdResult;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCheckSheetId() {
		return checkSheetId;
	}

	public void setCheckSheetId(Long checkSheetId) {
		this.checkSheetId = checkSheetId;
	}

	public Long getRegisterBillId() {
		return registerBillId;
	}

	public void setRegisterBillId(Long registerBillId) {
		this.registerBillId = registerBillId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public Integer getDetectState() {
		return detectState;
	}

	public void setDetectState(Integer detectState) {
		this.detectState = detectState;
	}

	public String getProductAliasName() {
		return productAliasName;
	}

	public void setProductAliasName(String productAliasName) {
		this.productAliasName = productAliasName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getDetectStateView() {
		return detectStateView;
	}

	public void setDetectStateView(String detectStateView) {
		this.detectStateView = detectStateView;
	}

	public String getLatestPdResult() {
		return latestPdResult;
	}

	public void setLatestPdResult(String latestPdResult) {
		this.latestPdResult = latestPdResult;
	}

}
