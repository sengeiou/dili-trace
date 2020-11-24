package com.dili.trace.dto;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.dili.trace.domain.*;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import org.apache.commons.beanutils.BeanUtils;

import com.dili.common.exception.TraceBizException;

public class RegisterBillOutputDto extends RegisterBill {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<TradeDetail> tradeDetailList;
	private List<ImageCert> imageCertList;
	private String upStreamName;
	private List<SeparateSalesRecord>separateSalesRecords;
	List<QualityTraceTradeBill> qualityTraceTradeBillList;

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
	 * @return List<ImageCert> return the imageCertList
	 */
	public List<ImageCert> getImageCertList() {
		return imageCertList;
	}

	/**
	 * @param imageCertList the imageCertList to set
	 */
	public void setImageCertList(List<ImageCert> imageCertList) {
		this.imageCertList = imageCertList;
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
