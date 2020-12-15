package com.dili.trace.api.output;

public class CountDetectStatusDto {
    /**
     * 检测状态
     */
    private Integer detectStatus;
    /**
     * 统计数量
     */
    private Integer cnt;

    public Integer getDetectStatus() {
        return detectStatus;
    }

    public void setDetectStatus(Integer detectStatus) {
        this.detectStatus = detectStatus;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
}
