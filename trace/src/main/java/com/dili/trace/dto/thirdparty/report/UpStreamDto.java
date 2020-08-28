package com.dili.trace.dto.thirdparty.report;

import java.util.List;

public class UpStreamDto {

    private String idCard;
    private String legalPerson;
    private String license;
    private String marketId = "330110800";
    private List<PzVo> pzVoList;
    private String qyName;
    private String tel;
    private String thirdAccountId;
    private String thirdUpId;
    private Integer type;

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

    public List<PzVo> getPzVoList() {
        return pzVoList;
    }

    public void setPzVoList(List<PzVo> pzVoList) {
        this.pzVoList = pzVoList;
    }

    public String getQyName() {
        return qyName;
    }

    public void setQyName(String qyName) {
        this.qyName = qyName;
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

    public String getThirdUpId() {
        return thirdUpId;
    }

    public void setThirdUpId(String thirdUpId) {
        this.thirdUpId = thirdUpId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static class PzVo{
        private String credentialName;
        private String end;
        private String picUrl;
        private String start;

        public String getCredentialName() {
            return credentialName;
        }

        public void setCredentialName(String credentialName) {
            this.credentialName = credentialName;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }
    }


}
