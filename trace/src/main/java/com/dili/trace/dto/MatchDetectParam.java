package com.dili.trace.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by laikui on 2019/7/26.
 */
public class MatchDetectParam implements Serializable {
    private String tradeNo;
    private String tallyAreaNo;
    private String productName;
    private String idCardNo;
    private Date start;
    private Date end;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTallyAreaNo() {
        return tallyAreaNo;
    }

    public void setTallyAreaNo(String tallyAreaNo) {
        this.tallyAreaNo = tallyAreaNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "MatchDetectParam{" +
                "tradeNo='" + tradeNo + '\'' +
                ", tallyAreaNo='" + tallyAreaNo + '\'' +
                ", productName='" + productName + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
