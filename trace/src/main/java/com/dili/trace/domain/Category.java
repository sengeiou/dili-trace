package com.dili.trace.domain;

import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.ss.domain.BaseDomain;

import java.util.Date;

public class Category extends BaseDomain {

	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 拼音全写
	 */
	private String pingying;

	/**
	 * 拼音简写
	 */
	private String pyInitials;

	/**
	 * 状态
	 */
	private Integer state;
	/**
	 * 父品类
	 */
	private Long parent;
	/**
	 * 品类路径
	 */
	private String path;
	/**
	 * 品类代码
	 */
	private String code;
	/**
	 * 全名
	 */
	private String fullName;
	/**
	 * 上一级ID
	 */
	private Long parentId;
	/**
	 * 层级
	 */
	private Integer level;
	/**
	 * 创建时间
	 */
	private Date created;
	/**
	 * 修改时间
	 */
	private Date modified;
	/**
	 * 登记显示
	 */
	private Integer isShow;
	/**
	 * 市场id
	 */
	private Long marketId;
	/**
	 * 商品类型
	 */
	private Integer type;
	/**
	 * 商品规格名
	 */
	private String specification;
	/**
	 * 父级第三方编码
	 */
	private String parentCode;

	public static Category convert(CusCategoryDTO dto) {
		Category category = new Category();
		category.setId(dto.getId());
		category.setCode(dto.getKeycode());
		category.setName(dto.getName());
		category.setParent(dto.getParent());
		category.setPath(dto.getPath());
		category.setPingying(dto.getPingying());
		category.setPyInitials(dto.getPyInitials());
		category.setState(dto.getState());
		return category;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPingying(String pingying) {
		this.pingying = pingying;
	}

	public String getPingying() {
		return this.pingying;
	}

	public void setPyInitials(String pyInitials) {
		this.pyInitials = pyInitials;
	}

	public String getPyInitials() {
		return this.pyInitials;
	}


	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Long getParent() {
		return this.parent;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Long getMarketId() {
		return marketId;
	}

	public void setMarketId(Long marketId) {
		this.marketId = marketId;
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

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Category [");
		sb.append("id = ");
		sb.append(id);
		sb.append(", name = ");
		sb.append(name);
		sb.append(", code = ");
		sb.append(code);
		sb.append(", parent_code = ");
		sb.append(parentCode);
		sb.append(", market_id = ");
		sb.append(marketId);
		sb.append(", level = ");
		sb.append(level);
		sb.append(", pingying = ");
		sb.append(pingying);
		sb.append(", pyInitials = ");
		sb.append(pyInitials);
		sb.append(", state = ");
		sb.append(state);
		sb.append(", parent = ");
		sb.append(parent);
		sb.append(", path = ");
		sb.append(path);
		sb.append("]");
		return sb.toString();
	}
}