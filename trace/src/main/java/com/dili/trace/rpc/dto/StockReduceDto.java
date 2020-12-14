package com.dili.trace.rpc.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品批次库存表(Stock)实体类
 *
 * @author miaoguoxin
 * @since 2020-11-25 13:23:06
 */
public class StockReduceDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**库存业务号*/
    @NotNull(message = "库存业务号不能空")
    @Min(value = 1, message = "库存业务号不能为负数")
    private Long stockId;
    /**扣减库存  */
    @NotNull(message = "扣减数量不能为空")
    @Min(value = 1, message = "扣减库存数量不能为负数")
    private BigDecimal reduceNum;
    /**扣减库存关联的业务单号 */
    @NotNull(message = "业务号不能为空")
    private Long bizId;

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public BigDecimal getReduceNum() {
        return reduceNum;
    }

    public void setReduceNum(BigDecimal reduceNum) {
        this.reduceNum = reduceNum;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }
}