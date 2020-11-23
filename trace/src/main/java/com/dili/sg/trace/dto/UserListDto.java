package com.dili.trace.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.User;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
public class UserListDto extends User {
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private Date createdStart;

    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private Date createdEnd;

    /**
     * 昵称模糊查询
     * @return
     */
    @Column(name = "tally_area_nos")
    @Like
    private String likeTallyAreaNos;
    
    @Transient
    private Boolean hasBusinessLicense;

	public Date getCreatedStart() {
		return createdStart;
	}

	public void setCreatedStart(Date createdStart) {
		this.createdStart = createdStart;
	}

	public Date getCreatedEnd() {
		return createdEnd;
	}

	public void setCreatedEnd(Date createdEnd) {
		this.createdEnd = createdEnd;
	}

	public String getLikeTallyAreaNos() {
		return likeTallyAreaNos;
	}

	public void setLikeTallyAreaNos(String likeTallyAreaNos) {
		this.likeTallyAreaNos = likeTallyAreaNos;
	}

	public Boolean getHasBusinessLicense() {
		return hasBusinessLicense;
	}

	public void setHasBusinessLicense(Boolean hasBusinessLicense) {
		this.hasBusinessLicense = hasBusinessLicense;
	}

}