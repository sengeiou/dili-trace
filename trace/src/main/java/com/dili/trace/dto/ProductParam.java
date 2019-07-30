package com.dili.trace.dto;

import java.io.Serializable;

/**
 * Created by laikui on 2019/7/26.
 */
public class ProductParam implements Serializable{
    private String productName;
    private Long productId;
    private String originName;
    private Long originId;
    private Long weight;
    private String originCertifiyUrl;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getOriginCertifiyUrl() {
        return originCertifiyUrl;
    }

    public void setOriginCertifiyUrl(String originCertifiyUrl) {
        this.originCertifiyUrl = originCertifiyUrl;
    }

    @Override
    public String toString() {
        return "ProductParam{" +
                "productName='" + productName + '\'' +
                ", productId=" + productId +
                ", originName='" + originName + '\'' +
                ", originId=" + originId +
                ", weight=" + weight +
                ", originCertifiyUrl='" + originCertifiyUrl + '\'' +
                '}';
    }
}
