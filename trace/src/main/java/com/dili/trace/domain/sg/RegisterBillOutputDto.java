package com.dili.trace.domain.sg;

import com.dili.sg.trace.domain.RegisterBill;
import com.dili.sg.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.DetectRecord;

import java.util.List;

/**
 * Created by laikui on 2019/7/30.
 */
public class RegisterBillOutputDto extends RegisterBill {
    private List<SeparateSalesRecord> separateSalesRecords;

    private DetectRecord detectRecord;
    
    private List<QualityTraceTradeBill> qualityTraceTradeBillList;

    public List<SeparateSalesRecord> getSeparateSalesRecords() {
        return separateSalesRecords;
    }

    public void setSeparateSalesRecords(List<SeparateSalesRecord> separateSalesRecords) {
        this.separateSalesRecords = separateSalesRecords;
    }

    public DetectRecord getDetectRecord() {
        return detectRecord;
    }

    public void setDetectRecord(DetectRecord detectRecord) {
        this.detectRecord = detectRecord;
    }

    public List<QualityTraceTradeBill> getQualityTraceTradeBillList() {
        return qualityTraceTradeBillList;
    }

    public void setQualityTraceTradeBillList(List<QualityTraceTradeBill> qualityTraceTradeBillList) {
        this.qualityTraceTradeBillList = qualityTraceTradeBillList;
    }
    
}
