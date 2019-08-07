package com.dili.trace.dto;


import com.dili.trace.domain.City;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.List;


/**
 *  <br />
 * @createTime 2017-6-15 12:07:08
 * @author template
 */
public class CityListInput extends City implements Serializable{


    /**
     * 查询级别
     */
    private List<Integer> levelTypes;

    /**
     * 关键字匹配
     */
    private String keyword;

    public List<Integer> getLevelTypes() {
        return levelTypes;
    }

    public void setLevelTypes(List<Integer> levelTypes) {
        this.levelTypes = levelTypes;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}