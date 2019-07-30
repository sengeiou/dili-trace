package com.dili.trace.dto;

import com.dili.trace.domain.RegisterBill;

import java.util.List;

/**
 * Created by laikui on 2019/7/30.
 */
public interface RegisterBillDto extends RegisterBill {
    String getCreatedStart();
    void setCreatedStart(String createdStart);

    String getCreatedEnd();
    void setCreatedEnd(String createdEnd);

    String getLatestDetectTimeTimeStart();
    void setLatestDetectTimeTimeStart(String latestDetectTimeTimeStart);

    String getLatestDetectTimeTimeEnd();
    void setLatestDetectTimeTimeEnd(String latestDetectTimeTimeEnd);

    String getPlateMatch();
    void setPlateMatch(String plateMatch);

    String getProductNameMatch();
    void setProductNameMatch(String productNameMatch);

    String getRecordGreeterNameMatch();
    void setRecordGreeterNameMatch(String recordGreeterNameMatch);

    String getAttr();
    void setAttr(String attr);

    String getAttrValue();
    void setAttrValue(String attrValue);
}
