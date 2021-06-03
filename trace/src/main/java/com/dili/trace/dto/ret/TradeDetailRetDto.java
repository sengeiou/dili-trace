package com.dili.trace.dto.ret;

import com.dili.trace.domain.TradeDetail;

public class TradeDetailRetDto extends TradeDetail {
    private Integer detectResult;

    public Integer getDetectResult() {
        return detectResult;
    }

    public void setDetectResult(Integer detectResult) {
        this.detectResult = detectResult;
    }
}
