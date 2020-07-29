package com.dili.trace.dto;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.ThirdPartyReportData;

import io.swagger.annotations.ApiModelProperty;

public class ThirdPartyReportDataQueryDto extends ThirdPartyReportData {
    @ApiModelProperty(value = "查询登记开始时间")
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private String createdStart;

    @ApiModelProperty(value = "查询登记结束时间")
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private String createdEnd;
    
	@ApiModelProperty(value = "上报名称LIKE")
	@Column(name = "`name`")
	@Like(value = "BOTH")
    private String likeName;
    

    @ApiModelProperty(value = "操作员名称")
	@Column(name = "`operator_name`")
	@Like(value = "BOTH")
    private String likeOperatorName;

    /**
     * @return String return the createdStart
     */
    public String getCreatedStart() {
        return createdStart;
    }

    /**
     * @param createdStart the createdStart to set
     */
    public void setCreatedStart(String createdStart) {
        this.createdStart = createdStart;
    }

    /**
     * @return String return the createdEnd
     */
    public String getCreatedEnd() {
        return createdEnd;
    }

    /**
     * @param createdEnd the createdEnd to set
     */
    public void setCreatedEnd(String createdEnd) {
        this.createdEnd = createdEnd;
    }


    /**
     * @return String return the likeName
     */
    public String getLikeName() {
        return likeName;
    }

    /**
     * @param likeName the likeName to set
     */
    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    /**
     * @return String return the likeOperatorName
     */
    public String getLikeOperatorName() {
        return likeOperatorName;
    }

    /**
     * @param likeOperatorName the likeOperatorName to set
     */
    public void setLikeOperatorName(String likeOperatorName) {
        this.likeOperatorName = likeOperatorName;
    }

}