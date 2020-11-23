package com.dili.trace.domain;

import com.dili.assets.sdk.dto.TradeTypeDto;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * <p>
 * This file was generated on 2019-07-31 14:56:14.
 */
public class TradeType {


    private String typeId;
    private String typeName;

    public static TradeType convert(TradeTypeDto dto) {

        TradeType tradeType = new TradeType();
        tradeType.setTypeName(dto.getName());
        tradeType.setTypeId(dto.getCode());
        return tradeType;

    }


    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}