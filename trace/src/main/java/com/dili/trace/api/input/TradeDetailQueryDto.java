package com.dili.trace.api.input;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.TradeDetail;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.util.List;

public class TradeDetailQueryDto extends TradeDetail {
    /**
     * 查询登记开始时间
     */
    @ApiModelProperty(value = "查询登记开始时间")
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private String createdStart;

    /**
     * 查询登记结束时间
     */
    @ApiModelProperty(value = "查询登记结束时间")
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private String createdEnd;


    @Column(name = "`stock_weight`")
    @Operator(Operator.GREAT_THAN)
    private BigDecimal minStockWeight;


    @Column(name = "`trade_request_id`")
    @Operator(Operator.IN)
    private List<Long> tradeRequestIdList;


    @Column(name = "`detect_result`")
    @Operator(Operator.IN)
    private List<Integer> detectResultList;

    public List<Integer> getDetectResultList() {
        return detectResultList;
    }

    public void setDetectResultList(List<Integer> detectResultList) {
        this.detectResultList = detectResultList;
    }

    public List<Long> getTradeRequestIdList() {
        return tradeRequestIdList;
    }

    public void setTradeRequestIdList(List<Long> tradeRequestIdList) {
        this.tradeRequestIdList = tradeRequestIdList;
    }

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

    public BigDecimal getMinStockWeight() {
        return minStockWeight;
    }

    public void setMinStockWeight(BigDecimal minStockWeight) {
        this.minStockWeight = minStockWeight;
    }
}