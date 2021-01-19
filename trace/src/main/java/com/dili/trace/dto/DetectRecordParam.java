package com.dili.trace.dto;

import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.domain.DetectRecord;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;

/**
 * Created by laikui on 2019/8/15.
 */
public class DetectRecordParam extends DetectRecord {
    /**
     * 仪器编号
     *
     * @return
     */
    @ApiModelProperty(value = "仪器编号")
    private String exeMachineNo;

    /**
     * 环境标记
     *
     * @return
     */
    @ApiModelProperty(value = "环境标记")
    private String tag;

    /**
     * 采样编号
     *
     * @return
     */
    @Transient
    private String sampleCode;

    public String getExeMachineNo() {
        return exeMachineNo;
    }

    public void setExeMachineNo(String exeMachineNo) {
        this.exeMachineNo = exeMachineNo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }
}
