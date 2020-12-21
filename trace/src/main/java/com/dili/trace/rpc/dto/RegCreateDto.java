package com.dili.trace.rpc.dto;

import java.util.List;

public class RegCreateDto {

    /**
     * 入库单详情
     */
    private List<RegDetailDto> regDetailDtos;
    /**
     * 市场id
     */
    private Long firmId;
    /**
     * 车牌号
     */
    private String plateNo;
    /**
     * 入库单号  来源报备单  进门单  等等  id
     */
    private String inStockNo;

    /**
     * 市场名称
     */
    private String firmName;

    /**
     * 入库单来源   默认1
     */
    private Integer source;
    /**
     * 创建员账号
     */
    private Long operatorId;

    /**
     * 创建员姓名
     */
    private String operatorName;

    public List<RegDetailDto> getRegDetailDtos() {
        return regDetailDtos;
    }

    public void setRegDetailDtos(List<RegDetailDto> regDetailDtos) {
        this.regDetailDtos = regDetailDtos;
    }

    public Long getFirmId() {
        return firmId;
    }

    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getInStockNo() {
        return inStockNo;
    }

    public void setInStockNo(String inStockNo) {
        this.inStockNo = inStockNo;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
