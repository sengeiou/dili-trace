package com.dili.trace.api.input;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.UserPlate;

/**
 * 客户车牌查询DTO
 */
public class UserPlateQueryDto extends UserPlate {
    /**
     * 车牌号模糊查询
     *
     * @return
     */
    @Column(name = "`plate`")
    @Like(value = "RIGHT")
    private String likePlate;

    public String getLikePlate() {
        return likePlate;
    }

    public void setLikePlate(String likePlate) {
        this.likePlate = likePlate;
    }
}