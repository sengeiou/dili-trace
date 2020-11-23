package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.Category;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import javax.persistence.Column;

/**
 * 品类 <br />
 *
 * @createTime 2016-12-23 10:31:51
 * @author template
 */
public class CategoryListInput extends Category implements Serializable {

    /**
     * 名称(精确匹配)、简称（精确匹配）、code（精确）
     */
    @ApiModelProperty(value = "名称(精确匹配)、简称（精确匹配）、code（精确）")
    @Column(name = "`name`")
    @Operator(value = Operator.EQUAL)
    private String keyWordStr;
    /**
     * 名称(后模糊)、简称（后模糊）、code（精确）
     */
    @ApiModelProperty(value = "名称(后模糊)、简称（后模糊）、code（精确）")
    @Column(name = "`name`")
    @Like(value = "RIGHT")
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
