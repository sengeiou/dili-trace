package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.dto.IMybatisForceParams;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.domain.RegisterBill;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by laikui on 2019/7/30.
 */
public interface RegisterBillDto extends RegisterBill, IMybatisForceParams {
    @ApiModelProperty(value = "查询登记开始时间")
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    String getCreatedStart();
    void setCreatedStart(String createdStart);

    @ApiModelProperty(value = "查询登记结束时间")
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    String getCreatedEnd();
    void setCreatedEnd(String createdEnd);

    @ApiModelProperty(value = "查询检测开始时间")
    @Column(name = "`latest_detect_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    String getLatestDetectTimeTimeStart();
    void setLatestDetectTimeTimeStart(String latestDetectTimeTimeStart);
    @ApiModelProperty(value = "查询检测结束时间")
    @Column(name = "`latest_detect_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    String getLatestDetectTimeTimeEnd();
    void setLatestDetectTimeTimeEnd(String latestDetectTimeTimeEnd);

    @ApiModelProperty(value = "商品名称LIKE")
    @Column(name = "`product_name`")
    @Like(value="RIGHT")
    String getLikeProductName();

    void setLikeProductName(String productName);

    @Transient
    String getAttr();
    void setAttr(String attr);
    @Transient
    String getAttrValue();
    void setAttrValue(String attrValue);
    
    @Transient
    Boolean getHasReport();
    void setHasReport(Boolean hasReport);
    
    @Column(name = "`state`")
    @Operator(Operator.IN)
    List<Integer> getStateList();
    void setStateList(List<Integer> stateList);
    
    @Column(name = "`detect_state`")
    @Operator(Operator.IN)
    List<Integer> getDetectStateList();
    void setDetectStateList(List<Integer> detectStateList);
    
}
