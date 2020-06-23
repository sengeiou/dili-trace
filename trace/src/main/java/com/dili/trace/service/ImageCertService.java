package com.dili.trace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.ImageCert;

import one.util.streamex.StreamEx;

@Service
public class ImageCertService extends BaseServiceImpl<ImageCert, Long> {
	public List<ImageCert> insertImageCert(List<ImageCert> imageCertList, Long billId) {
		if (billId != null) {
			// 先删除全部原有图片
			ImageCert deleteConditon = new ImageCert();
			deleteConditon.setTargetId(billId);
			this.deleteByExample(deleteConditon);

			// 增加新的图片
			return StreamEx.of(imageCertList).nonNull().map(cert -> {
				cert.setTargetId(billId);
				this.insertSelective(cert);
				return cert;
			}).toList();
		}
		return Lists.newArrayList(0);
	}
	public List<ImageCert> findImageCertListByBillId(Long billId){
		if(billId==null) {
			return com.google.common.collect.Lists.newArrayList();
		}
		ImageCert queryCondition = new ImageCert();
		queryCondition.setTargetId(billId);
		return this.listByExample(queryCondition);
	}

}
