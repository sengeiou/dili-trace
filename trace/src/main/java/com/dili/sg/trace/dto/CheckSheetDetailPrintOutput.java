package com.dili.trace.dto;

import com.dili.trace.domain.CheckSheetDetail;

public class CheckSheetDetailPrintOutput {



    private String productName;


    private String originName;


    private String productAliasName;

    private Integer orderNumber;

    private String latestPdResult;

    private String detectStateView;

    public static CheckSheetDetailPrintOutput build(CheckSheetDetail checkSheetDetai,String detectStateView) {
        CheckSheetDetailPrintOutput detailOutprint = new CheckSheetDetailPrintOutput();
        detailOutprint.setProductName(checkSheetDetai.getProductName());
        detailOutprint.setOriginName(checkSheetDetai.getOriginName());
        detailOutprint.setProductAliasName(checkSheetDetai.getProductAliasName());
        detailOutprint.setOrderNumber(checkSheetDetai.getOrderNumber());
        detailOutprint.setLatestPdResult(checkSheetDetai.getLatestPdResult());
        detailOutprint.setDetectStateView(detectStateView);
        return detailOutprint;
    }

    /**
     * @return String return the detectStateView
     */
    public String getDetectStateView() {
        return detectStateView;
    }

    /**
     * @param detectStateView the detectStateView to set
     */
    public void setDetectStateView(String detectStateView) {
        this.detectStateView = detectStateView;
    }


    /**
     * @return String return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }


   
    /**
     * @return String return the originName
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * @param originName the originName to set
     */
    public void setOriginName(String originName) {
        this.originName = originName;
    }

    /**
     * @return String return the productAliasName
     */
    public String getProductAliasName() {
        return productAliasName;
    }

    /**
     * @param productAliasName the productAliasName to set
     */
    public void setProductAliasName(String productAliasName) {
        this.productAliasName = productAliasName;
    }

    /**
     * @return Integer return the orderNumber
     */
    public Integer getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber the orderNumber to set
     */
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @return String return the latestPdResult
     */
    public String getLatestPdResult() {
        return latestPdResult;
    }

    /**
     * @param latestPdResult the latestPdResult to set
     */
    public void setLatestPdResult(String latestPdResult) {
        this.latestPdResult = latestPdResult;
    }

}