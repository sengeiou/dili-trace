package com.dili.trace.dto;


import com.dili.trace.domain.Category;

import io.swagger.annotations.ApiModelProperty;

/**
 * 品类 <br />
 * @createTime 2016-12-23 10:31:51
 * @author template
 */
public class CategoryListInput extends Category {
    /**
     * 名称(精确匹配)、简称（精确匹配）、code（精确）
     */
    @ApiModelProperty(value = "名称(精确匹配)、简称（精确匹配）、code（精确）")
    private String keyWordStr;
    /**
     * 名称(后模糊)、简称（后模糊）、code（精确）
     */
    @ApiModelProperty(value = "名称(后模糊)、简称（后模糊）、code（精确）")
    private String keyword;

    @ApiModelProperty(value = "层级")
    private Integer level;
    @ApiModelProperty(value = "上一级ID")
	private Long parentId;


    public String getKeyWordStr() {
        return keyWordStr;
    }

    public void setKeyWordStr(String keyWordStr) {
        this.keyWordStr = keyWordStr;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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


    /**
     * @return Long return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}