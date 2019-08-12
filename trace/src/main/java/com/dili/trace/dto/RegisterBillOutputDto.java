package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by laikui on 2019/7/30.
 */
public interface RegisterBillOutputDto extends RegisterBill {
    List<SeparateSalesRecord> getSeparateSalesRecords();
    void setSeparateSalesRecords(List<SeparateSalesRecord> separateSalesRecords);

    DetectRecord getDetectRecord();
    void setDetectRecord(DetectRecord detectRecord);

    QualityTraceTradeBill getQualityTraceTradeBill();
    void setQualityTraceTradeBill(QualityTraceTradeBill qualityTraceTradeBill);
}
