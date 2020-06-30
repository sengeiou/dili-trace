package com.dili.trace.api.input;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.TradeDetail;

import io.swagger.annotations.ApiModelProperty;

public class TradeDetailInputDto extends TradeDetail {
    @ApiModelProperty(value = "查询登记开始时间")
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private String createdStart;

    @ApiModelProperty(value = "查询登记结束时间")
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private String createdEnd;
    

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

}