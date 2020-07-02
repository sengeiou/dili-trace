package com.dili.trace.api.input;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.UserPlate;

public interface UserPlateQueryDto extends UserPlate {
    @Column(name = "`plate`")
	@Like(value = "RIGHT")
    String getLikePlate();

    void setLikePlate(String likePlate);
}