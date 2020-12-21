package com.dili.trace.rpc.dto;

import java.math.BigDecimal;
import java.util.Date;

public class StockReductResultDto {
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 冻结库存
     */
    private BigDecimal frozenNum;
    /**
     * 数据版本号
     */
    private Long version;

    /**
     * 批次详情单编号
     */
    private Long sheetItemId;

    /**
     * 创建开始时间
     */
    private Date createStartTime;
    /**
     * 创建结束时间
     */
    private Date createEndTime;
    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 库存业务号
     */
    private Long stockId;
    /**
     * 库存批次号
     */
    private Long sheetId;

    /**
     * 实际库存
     */
    private BigDecimal currentNum;

    /**
     * 市场id
     */
    private Long firmId;
    /**
     * 市场id
     */
    private String firmName;

    /**
     * {@link StockState}库存标志,1-有效库存 2-无效批次(售完,报损,离场)
     */
    private Integer state;
    /**
     * 主键id
     */
    private Long id;

    /**
     * 入库数量(斤)
     */
    private BigDecimal inStockNum;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getFrozenNum() {
        return frozenNum;
    }

    public void setFrozenNum(BigDecimal frozenNum) {
        this.frozenNum = frozenNum;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getSheetItemId() {
        return sheetItemId;
    }

    public void setSheetItemId(Long sheetItemId) {
        this.sheetItemId = sheetItemId;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getSheetId() {
        return sheetId;
    }

    public void setSheetId(Long sheetId) {
        this.sheetId = sheetId;
    }

    public BigDecimal getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(BigDecimal currentNum) {
        this.currentNum = currentNum;
    }

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getInStockNum() {
        return inStockNum;
    }

    public void setInStockNum(BigDecimal inStockNum) {
        this.inStockNum = inStockNum;
    }
}
