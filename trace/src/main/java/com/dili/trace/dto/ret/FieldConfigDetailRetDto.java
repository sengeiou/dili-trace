package com.dili.trace.dto.ret;

import com.dili.trace.domain.FieldConfigDetail;

public class FieldConfigDetailRetDto extends FieldConfigDetail {
    private String fieldId;
    /**
     * field name
     */
    private String fieldName;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
