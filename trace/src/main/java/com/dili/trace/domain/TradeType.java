package com.dili.trace.domain;

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
 * 
 * This file was generated on 2019-07-31 14:56:14.
 */
@Table(name = "`vTradeType`")
public interface TradeType extends IBaseDomain {


    void setId(Long id);

    @Column(name = "`type_id`")
    @FieldDef(label="typeId", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTypeId();

    void setTypeId(String typeId);

    @Column(name = "`type_name`")
    @FieldDef(label="001外省菜002本省菜003本地菜004落地菜005理货区", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTypeName();

    void setTypeName(String typeName);
}