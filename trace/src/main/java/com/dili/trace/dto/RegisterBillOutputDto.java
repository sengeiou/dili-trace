package com.dili.trace.dto;

import java.util.List;

import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;

/**
 * Created by laikui on 2019/7/30.
 */
public interface RegisterBillOutputDto extends RegisterBill {
    List<SeparateSalesRecord> getSeparateSalesRecords();
    void setSeparateSalesRecords(List<SeparateSalesRecord> separateSalesRecords);

    DetectRecord getDetectRecord();
    void setDetectRecord(DetectRecord detectRecord);

//    QualityTraceTradeBill getQualityTraceTradeBill();
//    void setQualityTraceTradeBill(QualityTraceTradeBill qualityTraceTradeBill);
    
    
    List<QualityTraceTradeBill> getQualityTraceTradeBillList();
    void setQualityTraceTradeBillList(List<QualityTraceTradeBill> qualityTraceTradeBillList);
}
