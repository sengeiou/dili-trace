package com.dili.trace.api.output;

import java.util.List;

import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.TradeDetail;

public class TraceDetailOutputDto {
	private List<ImageCert> imageCertList;
	// private List<String> originNameList;
	private String productName;
	private String specName;
	private String brandName;
	private List<TradeDetail> tradeDetailList;

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

	// /**
	// * @return List<String> return the originNameList
	// */
	// public List<String> getOriginNameList() {
	// return originNameList;
	// }

	// /**
	// * @param originNameList the originNameList to set
	// */
	// public void setOriginNameList(List<String> originNameList) {
	// this.originNameList = originNameList;
	// }

	/**
	 * @return String return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return String return the specName
	 */
	public String getSpecName() {
		return specName;
	}

	/**
	 * @param specName the specName to set
	 */
	public void setSpecName(String specName) {
		this.specName = specName;
	}

	/**
	 * @return String return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


    /**
     * @return List<TradeDetail> return the tradeDetailList
     */
    public List<TradeDetail> getTradeDetailList() {
        return tradeDetailList;
    }

    /**
     * @param tradeDetailList the tradeDetailList to set
     */
    public void setTradeDetailList(List<TradeDetail> tradeDetailList) {
        this.tradeDetailList = tradeDetailList;
    }

}
