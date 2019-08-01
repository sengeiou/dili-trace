package com.dili.trace.domain;


/**
 * 品类 <br />
 * @createTime 2016-12-23 10:31:51
 * @author template
 */public class Category {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 名称(精确匹配)
     */
    private String fullName;

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
    private Long status;

    /**
     * 父品类
     */
    private Long parent;

    /**
     * 品类路径
     */
    private String path;
    //品类代码
    private String code;

    public void setName (String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPingying (String pingying){
        this.pingying = pingying;
    }
    public String getPingying(){
        return this.pingying;
    }
    public void setPyInitials (String pyInitials){
        this.pyInitials = pyInitials;
    }
    public String getPyInitials(){
        return this.pyInitials;
    }
    public void setStatus (Long status){
        this.status = status;
    }
    public Long getStatus(){
        return this.status;
    }
    public void setParent (Long parent){
        this.parent = parent;
    }
    public Long getParent(){
        return this.parent;
    }
    public void setPath (String path){
        this.path = path;
    }
    public String getPath(){
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

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("Category [");
        sb.append("id = ");
        sb.append(id);
        sb.append(", name = ");
        sb.append(name);
        sb.append(", fullName = ");
        sb.append(fullName);
        sb.append(", pingying = ");
        sb.append(pingying);
        sb.append(", pyInitials = ");
        sb.append(pyInitials);
        sb.append(", status = ");
        sb.append(status);
        sb.append(", parent = ");
        sb.append(parent);
        sb.append(", path = ");
        sb.append(path);
        sb.append("]");
        return sb.toString();
    }
}