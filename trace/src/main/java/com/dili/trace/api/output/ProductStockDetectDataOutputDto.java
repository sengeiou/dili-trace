package com.dili.trace.api.output;

import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.ImageCert;

import java.util.List;

public class ProductStockDetectDataOutputDto {
    private Long billId;
    /**
     * 批次号
     */
    private String batchNo;

    private DetectRecord detectRecord;

    private List<ImageCert> imageCertList;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public DetectRecord getDetectRecord() {
        return detectRecord;
    }

    public void setDetectRecord(DetectRecord detectRecord) {
        this.detectRecord = detectRecord;
    }

    public List<ImageCert> getImageCertList() {
        return imageCertList;
    }

    public void setImageCertList(List<ImageCert> imageCertList) {
        this.imageCertList = imageCertList;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }
}
