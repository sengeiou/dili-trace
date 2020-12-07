package com.dili.trace.api.output;

import java.util.Date;
import java.util.List;

import com.dili.trace.domain.ImageCert;

public class TraceDetailOutputDto {
    /**
     * 交易單requestId
     */
    private Long tradeRequestId;
    /**
     * 图片集合
     */
    private List<ImageCert> imageCertList;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 规格名称
     */
    private String specName;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 上游数据集合
     */
    private List<TraceDataDto> upTraceList;
    /**
     * 下游数据集合
     */
    private List<TraceDataDto> downTraceList;

    /**
     * @return List<ImageCert> return the imageCertList
     */
    public List<ImageCert> getImageCertList() {
        return imageCertList;
    }

    /**
     * @param imageCertList the imageCertList to set
     */
    public void setImageCertList(List<ImageCert> imageCertList) {
        this.imageCertList = imageCertList;
    }

    // /**
    // * @return List<String> return the originNameList
    // */
    // public List<String> getOriginNameList() {
    // return originNameList;
    // }

    // /**
    // * @param originNameList the originNameList to set
    // */
    // public void setOriginNameList(List<String> originNameList) {
    // this.originNameList = originNameList;
    // }

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
     * @return String return the specName
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * @param specName the specName to set
     */
    public void setSpecName(String specName) {
        this.specName = specName;
    }

    /**
     * @return String return the brandName
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * @param brandName the brandName to set
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    /**
     * @return List<TraceDataDto> return the downTraceList
     */
    public List<TraceDataDto> getDownTraceList() {
        return downTraceList;
    }

    /**
     * @param downTraceList the downTraceList to set
     */
    public void setDownTraceList(List<TraceDataDto> downTraceList) {
        this.downTraceList = downTraceList;
    }


    /**
     * @return Long return the tradeRequestId
     */
    public Long getTradeRequestId() {
        return tradeRequestId;
    }

    /**
     * @param tradeRequestId the tradeRequestId to set
     */
    public void setTradeRequestId(Long tradeRequestId) {
        this.tradeRequestId = tradeRequestId;
    }


    /**
     * @return Date return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }


    /**
     * @return List<TraceDataDto> return the upTraceList
     */
    public List<TraceDataDto> getUpTraceList() {
        return upTraceList;
    }

    /**
     * @param upTraceList the upTraceList to set
     */
    public void setUpTraceList(List<TraceDataDto> upTraceList) {
        this.upTraceList = upTraceList;
    }

}
