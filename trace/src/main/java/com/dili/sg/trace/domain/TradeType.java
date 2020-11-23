package com.dili.sg.trace.domain;

import com.dili.assets.sdk.dto.TradeTypeDto;

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