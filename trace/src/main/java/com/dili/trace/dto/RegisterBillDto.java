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

    void setLikeProductName(String likeProductName);

    @Transient
    String getAttr();
    void setAttr(String attr);
    @Transient
    String getAttrValue();
    void setAttrValue(String attrValue);
    
//    @Transient
//    Boolean getHasReport();
//    void setHasReport(Boolean hasReport);
    
    @Transient
    Boolean getHasDetectReport();
    void setHasDetectReport(Boolean hasDetectReport);
    
    @Transient
    Boolean getHasHandleResult();
    void setHasHandleResult(Boolean hasHandleResult);
    
    
    @Transient
    Boolean getHasOriginCertifiy();
    void setHasOriginCertifiy(Boolean hasOriginCertifiy);

    
    @Column(name = "`state`")
    @Operator(Operator.IN)
    List<Integer> getStateList();
    void setStateList(List<Integer> stateList);
    
    @Column(name = "`detect_state`")
    @Operator(Operator.IN)
    List<Integer> getDetectStateList();
    void setDetectStateList(List<Integer> detectStateList);
    
    /**
     * 昵称模糊查询
     * @return
     */
    @Column(name = "tally_area_no")
    @Like
    String getLikeTallyAreaNo();
    void setLikeTallyAreaNo(String likeTallyAreaNo);
    

    @Column(name = "`sample_code`")
    @Like
    String getLikeSampleCode();
    void setLikeSampleCode(String likeSampleCode);
    
    
    @Column(name = "`sample_code`")
    @Operator(Operator.IN)
    List<String> getSampleCodeList();
    void setSampleCodeList(List<String> sampleCodeList);
    
    
    @ApiModelProperty(value = "IN商品ID")
    @Column(name = "`product_id`")
    @FieldDef(label="productId")
    @EditMode(editor = FieldEditor.Number, required = true)
    List<Long> getProductIdList();

    void setProductIdList(List<Long> productIdList);
    
    
    @ApiModelProperty(value = "IN ID")
    @Column(name = "`id`")
    @FieldDef(label="id")
    @Operator(Operator.IN)
    List<Long> getIdList();

    void setIdList(List<Long> idList);
    
    
    @ApiModelProperty(value = "车牌LIKE")
    @Column(name = "`plate`")
    @Like(value="RIGHT")
    String getLikePlate();

    void setLikePlate(String likePlate);
    
    @Transient
    String getTag();
    void setTag(String tag);
    
    
    @Transient
    String getAliasName();
    void setAliasName(String aliasName);

    
}
