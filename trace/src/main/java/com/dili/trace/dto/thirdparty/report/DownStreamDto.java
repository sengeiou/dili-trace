package com.dili.trace.dto.thirdparty.report;

import java.util.List;

public class DownStreamDto {
    private List<DownStreamImg> downStreamImgList;
    private String idCard;
    private String legalPerson;
    private String license;
    private String marketId = "330110800";
    private String name;
    private String streamName;
    private String tel;
    private String thirdAccountId;
    private String thirdDsId;
    private Integer type;

    public List<DownStreamImg> getDownStreamImgList() {
        return downStreamImgList;
    }

    public void setDownStreamImgList(List<DownStreamImg> downStreamImgList) {
        this.downStreamImgList = downStreamImgList;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getThirdAccountId() {
        return thirdAccountId;
    }

    public void setThirdAccountId(String thirdAccountId) {
        this.thirdAccountId = thirdAccountId;
    }

    public String getThirdDsId() {
        return thirdDsId;
    }

    public void setThirdDsId(String thirdDsId) {
        this.thirdDsId = thirdDsId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


}
