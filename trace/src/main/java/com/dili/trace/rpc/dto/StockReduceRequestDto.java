package com.dili.trace.rpc.dto;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/12/2 10:08
 */
public class StockReduceRequestDto implements Serializable {
    /**市场id*/
    @NotNull(message = "市场id不能为空")
    private Long firmId;
    /**市场名称*/
    @NotNull(message = "市场名称不能为空")
    private String firmName;
    /**扣减库存项*/
    @Valid
    @NotNull(message = "库存扣减参数缺失")
    private List<StockReduceDto> stockReduceItems;

    public Long getFirmId() {
        return firmId;
    }

    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public List<StockReduceDto> getStockReduceItems() {
        return stockReduceItems;
    }

    public void setStockReduceItems(List<StockReduceDto> stockReduceItems) {
        this.stockReduceItems = stockReduceItems;
    }
}
