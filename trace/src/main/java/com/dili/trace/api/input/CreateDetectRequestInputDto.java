package com.dili.trace.api.input;

import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.DetectRequest;

public class CreateDetectRequestInputDto {
    private DetectRequest detectRequest;
    private DetectRecord detectRecord;

    public DetectRequest getDetectRequest() {
        return detectRequest;
    }

    public void setDetectRequest(DetectRequest detectRequest) {
        this.detectRequest = detectRequest;
    }

    public DetectRecord getDetectRecord() {
        return detectRecord;
    }

    public void setDetectRecord(DetectRecord detectRecord) {
        this.detectRecord = detectRecord;
    }
}
