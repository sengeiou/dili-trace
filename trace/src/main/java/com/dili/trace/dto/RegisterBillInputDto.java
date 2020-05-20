package com.dili.trace.dto;

import java.util.List;

public class RegisterBillInputDto {
    private Long userId;
    private List<Long> upStreamIdList;
    private List<Long> productIdList;
    private List<String> productNameList;
    private List<Integer> weightList;
    private List<Long> idList;
    private List<String> originCertifiyUrlList;
    private List<String> detectReportUrlList;

    private List<Long> originIdList;
    private List<String> originNameList;

    /**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return List<Long> return the upStreamIdList
     */
    public List<Long> getUpStreamIdList() {
        return upStreamIdList;
    }

    /**
     * @param upStreamIdList the upStreamIdList to set
     */
    public void setUpStreamIdList(List<Long> upStreamIdList) {
        this.upStreamIdList = upStreamIdList;
    }

    /**
     * @return List<Long> return the productIdList
     */
    public List<Long> getProductIdList() {
        return productIdList;
    }

    /**
     * @param productIdList the productIdList to set
     */
    public void setProductIdList(List<Long> productIdList) {
        this.productIdList = productIdList;
    }

    /**
     * @return List<Integer> return the weightList
     */
    public List<Integer> getWeightList() {
        return weightList;
    }

    /**
     * @param weightList the weightList to set
     */
    public void setWeightList(List<Integer> weightList) {
        this.weightList = weightList;
    }

    /**
     * @return List<Long> return the idList
     */
    public List<Long> getIdList() {
        return idList;
    }

    /**
     * @param idList the idList to set
     */
    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    /**
     * @return List<String> return the originCertifiyUrlList
     */
    public List<String> getOriginCertifiyUrlList() {
        return originCertifiyUrlList;
    }

    /**
     * @param originCertifiyUrlList the originCertifiyUrlList to set
     */
    public void setOriginCertifiyUrlList(List<String> originCertifiyUrlList) {
        this.originCertifiyUrlList = originCertifiyUrlList;
    }

    /**
     * @return List<String> return the detectReportUrlList
     */
    public List<String> getDetectReportUrlList() {
        return detectReportUrlList;
    }

    /**
     * @param detectReportUrlList the detectReportUrlList to set
     */
    public void setDetectReportUrlList(List<String> detectReportUrlList) {
        this.detectReportUrlList = detectReportUrlList;
    }

    /**
     * @return List<String> return the productNameList
     */
    public List<String> getProductNameList() {
        return productNameList;
    }

    /**
     * @param productNameList the productNameList to set
     */
    public void setProductNameList(List<String> productNameList) {
        this.productNameList = productNameList;
    }


    /**
     * @return List<Long> return the originIdList
     */
    public List<Long> getOriginIdList() {
        return originIdList;
    }

    /**
     * @param originIdList the originIdList to set
     */
    public void setOriginIdList(List<Long> originIdList) {
        this.originIdList = originIdList;
    }

    /**
     * @return List<String> return the originNameList
     */
    public List<String> getOriginNameList() {
        return originNameList;
    }

    /**
     * @param originNameList the originNameList to set
     */
    public void setOriginNameList(List<String> originNameList) {
        this.originNameList = originNameList;
    }

}