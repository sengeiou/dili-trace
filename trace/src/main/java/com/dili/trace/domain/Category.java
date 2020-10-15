package com.dili.trace.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table(name = "`category`")
public class Category extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;
	@ApiModelProperty(value = "名称")
	@Column(name = "`name`")
	private String name;
	@ApiModelProperty(value = "全名")
	@Column(name = "`full_name`")
	private String fullName;

	@ApiModelProperty(value = "上一级ID")
	@Column(name = "`parent_id`")
	private Long parentId;

	@ApiModelProperty(value = "层级")
	@Column(name = "`level`")
	private Integer level;

	@ApiModelProperty(value = "创建时间")
	@Column(name = "`created`")
	private Date created;

	@ApiModelProperty(value = "修改时间")
	@Column(name = "`modified`")
	private Date modified;

	@ApiModelProperty(value = "市场ID")
	@Column(name = "market_id")
	private Long marketId;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	/**
	 * @return Integer return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getMarketId() {
		return marketId;
	}

	public void setMarketId(Long marketId) {
		this.marketId = marketId;
	}
}