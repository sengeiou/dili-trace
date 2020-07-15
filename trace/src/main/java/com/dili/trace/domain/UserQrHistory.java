package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`user_qr_history`")
public class UserQrHistory extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 二维码条目类型
     * {@link com.dili.trace.glossary.QrItemTypeEnum}
     */
    @Column(name = "`qr_item_type`")
    private Integer qrItemType;
    
    @Column(name = "`has_data`")
    private Integer hasData;
    
    @Column(name = "`valid`")
    private Integer valid;

    /**
     * 二维码条目参数值
     */
    @Column(name = "`objects`")
    private String objects;
    
    /**
     * 
     * 二维码条目状态
     * {@link com.dili.trace.glossary.ColorEnum}
     */
    @Column(name = "`color`")
    private Integer color;
    
    /**
     * 
     * 二维码条目操作
     * {@link com.dili.trace.glossary.QrItemActionEnum}
     */
    @Column(name = "`action`")
    private Integer action;
    

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`modified`")
    private Date created;

    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
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
     * @return Integer return the qrItemType
     */
    public Integer getQrItemType() {
        return qrItemType;
    }

    /**
     * @param qrItemType the qrItemType to set
     */
    public void setQrItemType(Integer qrItemType) {
        this.qrItemType = qrItemType;
    }



    public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
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

	public String getObjects() {
		return objects;
	}

	public void setObjects(String objects) {
		this.objects = objects;
	}

	public Integer getHasData() {
		return hasData;
	}

	public void setHasData(Integer hasData) {
		this.hasData = hasData;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

}