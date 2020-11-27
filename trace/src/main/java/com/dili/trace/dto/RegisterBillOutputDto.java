package com.dili.trace.dto;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.dili.trace.domain.*;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import one.util.streamex.StreamEx;
import org.apache.commons.beanutils.BeanUtils;

import com.dili.common.exception.TraceBizException;

public class RegisterBillOutputDto extends RegisterBill {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<TradeDetail> tradeDetailList;
	private String upStreamName;
	private List<SeparateSalesRecord>separateSalesRecords;
	List<QualityTraceTradeBill> qualityTraceTradeBillList;

	public Map<Integer,List<ImageCert>> getGroupedImageCertList(){
		return StreamEx.ofNullable(this.getImageCerts()).flatCollection(Function.identity())
				.mapToEntry(item-> item.getCertType(), Function.identity())
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
		return  detectRecord;
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
