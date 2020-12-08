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
public interface DetectRecordParam extends DetectRecord {
    /**
     * 仪器编号
     * @return
     */
    @ApiModelProperty(value = "仪器编号")
    @FieldDef(label="exeMachineNo")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getExeMachineNo();

    void setExeMachineNo(String exeMachineNo);

    /**
     * 环境标记
     * @return
     */
    @ApiModelProperty(value = "环境标记")
    @FieldDef(label="tag")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTag();

    void setTag(String tag);
    
    /**
     * 采样编号
     * @return
     */
    @Transient
    String getSampleCode();

    void setSampleCode(String sampleCode);
}
