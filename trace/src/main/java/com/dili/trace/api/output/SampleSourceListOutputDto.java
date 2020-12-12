package com.dili.trace.api.output;

import com.dili.trace.domain.RegisterBill;

public class SampleSourceListOutputDto extends RegisterBill {
    /**
     * 采样来源
     */
    private Integer sampleSource;

    public Integer getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(Integer sampleSource) {
        this.sampleSource = sampleSource;
    }
}