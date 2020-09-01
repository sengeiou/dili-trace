package com.dili.trace.dto.thirdparty.report;

import java.util.List;

/**
 * @author asa.lee
 */
public class ReportQrCodeDto {
    private String code;
    private Integer color;
    private String marketId;
    private String thirdAccId;
    private List<ReportQrCodeDetailDto> codeDetailList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getThirdAccId() {
        return thirdAccId;
    }

    public void setThirdAccId(String thirdAccId) {
        this.thirdAccId = thirdAccId;
    }

    public List<ReportQrCodeDetailDto> getCodeDetailList() {
        return codeDetailList;
    }

    public void setCodeDetailList(List<ReportQrCodeDetailDto> codeDetailList) {
        this.codeDetailList = codeDetailList;
    }
}
