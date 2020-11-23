package com.dili.trace.domain;

import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.ss.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class Category {

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
    //品类代码
    private String code;

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

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Category [");
        sb.append("id = ");
        sb.append(id);
        sb.append(", name = ");
        sb.append(name);

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
