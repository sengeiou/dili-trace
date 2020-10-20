package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
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

	@ApiModelProperty(value = "商品编码")
	@Column(name = "`code`")
	private String code;

	@ApiModelProperty(value = "登记显示")
	@Column(name = "`is_show`")
	private String isShow;

	@ApiModelProperty(value = "市场id")
	@Column(name = "`market_id`")
	private Long marketId;

    @ApiModelProperty(value = "商品类型")
    @Column(name = "`type`")
    private Integer type;

    @ApiModelProperty(value = "商品规格名")
    @Column(name = "`specification`")
    private String specification;

	@ApiModelProperty(value = "父级第三方编码")
	@Column(name = "`parent_code`")
	private String parentCode;

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public Long getMarketId() {
		return marketId;
	}

	public void setMarketId(Long marketId) {
		this.marketId = marketId;
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

}