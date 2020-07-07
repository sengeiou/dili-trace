package com.dili.trace.api.output;

import java.util.Date;

public class TraceDataDto {
    private String buyerName;
    private String sellerName;
    private String marketName;
    private String tallyAreaNo;
    private Date created;


    /**
     * @return String return the buyerName
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * @param buyerName the buyerName to set
     */
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    /**
     * @return String return the sellerName
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * @param sellerName the sellerName to set
     */
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    /**
     * @return String return the marketName
     */
    public String getMarketName() {
        return marketName;
    }

    /**
     * @param marketName the marketName to set
     */
    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    /**
     * @return String return the tallyAreaNo
     */
    public String getTallyAreaNo() {
        return tallyAreaNo;
    }

    /**
     * @param tallyAreaNo the tallyAreaNo to set
     */
    public void setTallyAreaNo(String tallyAreaNo) {
        this.tallyAreaNo = tallyAreaNo;
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

}