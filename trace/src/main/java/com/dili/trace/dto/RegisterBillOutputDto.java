package com.dili.trace.dto;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.trace.domain.*;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.enums.ImageCertTypeEnum;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.beanutils.BeanUtils;

import com.dili.common.exception.TraceBizException;
import org.apache.commons.lang3.StringUtils;

public class RegisterBillOutputDto extends RegisterBill {
    private static final long serialVersionUID = 1L;
    /**
     * 交易单明细集合
     */
    private List<TradeDetail> tradeDetailList;
    /**
     * 上游企业名称
     */
    private String upStreamName;
    /**
     * 单独销售记录
     */
    private List<SeparateSalesRecord> separateSalesRecords;
    List<QualityTraceTradeBill> qualityTraceTradeBillList;

    @JSONField(serialize = false)
    public String getHandleResultUrl() {
        return this.joinImageUrl(ImageCertTypeEnum.Handle_Result);
    }

    @JSONField(serialize = false)
    public String getOriginCertifiyUrl() {
        return this.joinImageUrl(ImageCertTypeEnum.ORIGIN_CERTIFIY);
    }
    @JSONField(serialize = false)
    public String getDetectReportUrl() {
        return this.joinImageUrl(ImageCertTypeEnum.DETECT_REPORT);
    }

    private String joinImageUrl(ImageCertTypeEnum certTypeEnum) {
        return
                this.getGroupedImageCertList().getOrDefault(ImageCertTypeEnum.Handle_Result, Lists.newArrayList())
                        .stream().map(ImageCert::getUid).filter(StringUtils::isNotBlank)
                        .collect(Collectors.joining(","));
    }
    @JSONField(serialize = false)
    public Map<ImageCertTypeEnum, List<ImageCert>> getGroupedImageCertList() {
        return StreamEx.ofNullable(this.getImageCertList()).flatCollection(Function.identity()).filter(item->item.getCertType()!=null)
                .mapToEntry(item -> ImageCertTypeEnum.fromCode(item.getCertType()).orElse(null), Function.identity())
                .grouping();
    }

    public List<QualityTraceTradeBill> getQualityTraceTradeBillList() {
        return qualityTraceTradeBillList;
    }

    public void setQualityTraceTradeBillList(List<QualityTraceTradeBill> qualityTraceTradeBillList) {
        this.qualityTraceTradeBillList = qualityTraceTradeBillList;
    }

    public List<SeparateSalesRecord> getSeparateSalesRecords() {
        return separateSalesRecords;
    }

    public void setSeparateSalesRecords(List<SeparateSalesRecord> separateSalesRecords) {
        this.separateSalesRecords = separateSalesRecords;
    }

    public List<TradeDetail> getTradeDetailList() {
        return tradeDetailList;
    }

    public void setTradeDetailList(List<TradeDetail> tradeDetailList) {
        this.tradeDetailList = tradeDetailList;
    }

    public static RegisterBillOutputDto build(RegisterBill registerBill, List<TradeDetail> tradeDetailList) {

        RegisterBillOutputDto dest = new RegisterBillOutputDto();
        try {
            BeanUtils.copyProperties(dest, registerBill);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TraceBizException("数据结构转换出错");
        }
        dest.setTradeDetailList(tradeDetailList);
        return dest;

    }

    private DetectRecord detectRecord;

    public DetectRecord getDetectRecord() {
        return detectRecord;
    }

    public void setDetectRecord(DetectRecord detectRecord) {
        this.detectRecord = detectRecord;
    }

    /**
     * @return String return the upStreamName
     */
    public String getUpStreamName() {
        return upStreamName;
    }

    /**
     * @param upStreamName the upStreamName to set
     */
    public void setUpStreamName(String upStreamName) {
        this.upStreamName = upStreamName;
    }

}
