package com.dili.sg.trace.domain;

import java.util.Date;

/**
 * 部门 <br />
 * @createTime 2016-10-12 18:17:05
 * @author template
 */public class Department{
		/** id */
		protected Long id;
    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 
     */
    private Integer yn;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 修改时间
     */
    private Date modified;

    /**
     * 市场
     */
    private Long marketId;
    /**
     * parentId
     */
    private Long parentId;
    
    public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public void setName (String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setRemarks (String remarks){
        this.remarks = remarks;
    }
    public String getRemarks(){
        return this.remarks;
    }
    public void setYn (Integer yn){
        this.yn = yn;
    }
    public Integer getYn(){
        return this.yn;
    }
    public void setCreated (Date created){
        this.created = created;
    }
    public Date getCreated(){
        return this.created;
    }
    public void setModified (Date modified){
        this.modified = modified;
    }
    public Date getModified(){
        return this.modified;
    }
    public void setMarketId (Long marketId){
        this.marketId = marketId;
    }
    public Long getMarketId(){
        return this.marketId;
    }

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("Department [");
        sb.append("id = ");
        sb.append(id);
        sb.append(", name = ");
        sb.append(name);
        sb.append(", remarks = ");
        sb.append(remarks);
        sb.append(", yn = ");
        sb.append(yn);
        sb.append(", created = ");
        sb.append(created);
        sb.append(", modified = ");
        sb.append(modified);
        sb.append(", marketId = ");
        sb.append(marketId);
        sb.append("]");
        return sb.toString();
    }
}