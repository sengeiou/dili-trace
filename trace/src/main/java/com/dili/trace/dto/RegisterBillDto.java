package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.domain.RegisterBill;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by laikui on 2019/7/30.
 */
public interface RegisterBillDto extends RegisterBill {
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    String getCreatedStart();
    void setCreatedStart(String createdStart);

    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    String getCreatedEnd();
    void setCreatedEnd(String createdEnd);

    @Column(name = "`latest_detect_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    String getLatestDetectTimeTimeStart();
    void setLatestDetectTimeTimeStart(String latestDetectTimeTimeStart);

    @Column(name = "`latest_detect_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    String getLatestDetectTimeTimeEnd();
    void setLatestDetectTimeTimeEnd(String latestDetectTimeTimeEnd);

    @Transient
    String getAttr();
    void setAttr(String attr);
    @Transient
    String getAttrValue();
    void setAttrValue(String attrValue);
}
