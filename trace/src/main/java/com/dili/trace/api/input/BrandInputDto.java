package com.dili.trace.api.input;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.Brand;

public class BrandInputDto extends Brand{
    @Column(name="brand_name")
    @Like(value = Like.BOTH)
    private String likeBrandName;


    /**
     * @return String return the likeBrandName
     */
    public String getLikeBrandName() {
        return likeBrandName;
    }

    /**
     * @param likeBrandName the likeBrandName to set
     */
    public void setLikeBrandName(String likeBrandName) {
        this.likeBrandName = likeBrandName;
    }

}