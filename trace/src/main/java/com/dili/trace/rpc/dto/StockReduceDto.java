package com.dili.trace.rpc.dto;

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
    private Long stockId;
    /**扣减库存  */
    private BigDecimal reduceNum;

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

}