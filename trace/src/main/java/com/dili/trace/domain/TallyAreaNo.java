package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

import com.dili.ss.domain.annotation.Like;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:34.
 * 摊位表
 */
@SuppressWarnings("serial")
@Table(name = "`tally_area_no`")
public class TallyAreaNo extends BaseDomain {

    /**
     * ID
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JsonIgnore
	private Long id;

    /**
     * 摊位号
     */
	@ApiModelProperty(value = "摊位号")
	@Column(name = "`number`")
    private String number;

    /**
     * 街区号
     */
	@ApiModelProperty(value = "街区号")
	@Column(name = "`street`")
	private String street;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域")
	@Column(name = "`area`")
    private String area;

    /**
     * 创建时间
     */
    @Column(name = "`created`")
	private Date created;

    /**
     * 修改时间
     */
	@Column(name = "`modified`")
    private Date modified;

    /**
     * 市场ID
     */
    @ApiModelProperty(value = "市场ID")
    @Column(name = "`market_id`")
    private String marketId;

    /**
     * 区位
     */
    @Column(name="area")
    @Like(value = Like.BOTH)
    private String likeAreaName;

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
     * @return String return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return String return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return String return the area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(String area) {
        this.area = area;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getLikeAreaName() {
        return likeAreaName;
    }

    public void setLikeAreaName(String likeAreaName) {
        this.likeAreaName = likeAreaName;
    }
}