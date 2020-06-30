package com.dili.trace.dto;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.dili.common.exception.TraceBusinessException;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.UpStream;

public class RegisterBillOutputDto extends RegisterBill {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<TradeDetail> tradeDetailList;
	private List<ImageCert> imageCertList;
	private UpStream upStream;

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
			throw new TraceBusinessException("数据结构转换出错");
		}
		dest.setTradeDetailList(tradeDetailList);
		return dest;

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
     * @return UpStream return the upStream
     */
    public UpStream getUpStream() {
        return upStream;
    }

    /**
     * @param upStream the upStream to set
     */
    public void setUpStream(UpStream upStream) {
        this.upStream = upStream;
    }

}
