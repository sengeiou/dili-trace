package com.dili.trace.domain.hangguo;

/**
 * 杭果返回实体
 *
 * @author asa.lee
 */
public class HangGuoResult {

    private String recode;
    private String errorinfo;
    private String data;

    public String getRecode() {
        return recode;
    }

    public void setRecode(String recode) {
        this.recode = recode;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
