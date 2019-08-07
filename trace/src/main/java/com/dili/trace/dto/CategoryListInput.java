package com.dili.trace.dto;


import com.dili.trace.domain.Category;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;

/**
 * 品类 <br />
 * @createTime 2016-12-23 10:31:51
 * @author template
 */
public class CategoryListInput extends Category implements Serializable{
    /**
     * 名称(精确匹配)、简称（精确匹配）
     */
    private String keyWordStr;
    /**
     * 关键字匹配
     */
    private String keyword;


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
}