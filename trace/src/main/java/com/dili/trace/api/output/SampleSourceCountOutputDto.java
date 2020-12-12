package com.dili.trace.api.output;

import com.dili.trace.glossary.SampleSourceEnum;

public class SampleSourceCountOutputDto {
    /**
     * 采样来源
     */
    private Integer sampleSource;

    /**
     * 采样来源名称
     */
    private String sampleSourceName;

    /**
     * 数量
     */
    private Integer num;

    public SampleSourceCountOutputDto() {
    }

    public SampleSourceCountOutputDto(Integer sampleSource, Integer num) {
        this.sampleSource = sampleSource;
        this.num = num;
    }

    public static SampleSourceCountOutputDto buildDefault(SampleSourceEnum sampleSource) {
        SampleSourceCountOutputDto dto = new SampleSourceCountOutputDto();
        dto.setNum(0);
        dto.setSampleSource(sampleSource.getCode());
        dto.setSampleSourceName(sampleSource.getName());
        return dto;
    }

    public Integer getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(Integer sampleSource) {
        this.sampleSource = sampleSource;
    }

    public String getSampleSourceName() {
        return sampleSourceName;
    }

    public void setSampleSourceName(String sampleSourceName) {
        this.sampleSourceName = sampleSourceName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}