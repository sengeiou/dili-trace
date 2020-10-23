package com.dili.trace.service;

import java.util.List;

import com.dili.trace.enums.BillTypeEnum;
import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.ImageCert;
import com.google.common.collect.Lists;

import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

@Service
public class ImageCertService extends BaseServiceImpl<ImageCert, Long> {
	public List<ImageCert> insertImageCert(List<ImageCert> imageCertList, Long billId) {
		if (billId != null) {
			// 先删除全部原有图片
			ImageCert deleteConditon = new ImageCert();
			deleteConditon.setBillId(billId);
			deleteConditon.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
			this.deleteByExample(deleteConditon);

			// 增加新的图片
			return StreamEx.of(imageCertList).nonNull().map(cert -> {
				cert.setBillId(billId);
				cert.setId(null);
				cert.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
				this.insertSelective(cert);
				return cert;
			}).toList();
		}
		return Lists.newArrayList();
	}

	public List<ImageCert> findImageCertListByBillId(Long billId) {
		if (billId == null) {
			return Lists.newArrayList();
		}
		ImageCert queryCondition = new ImageCert();
		queryCondition.setBillId(billId);
		return this.listByExample(queryCondition);
	}

	public List<ImageCert> findImageCertListByBillIdList(List<Long> billIdList) {
		if (billIdList == null || billIdList.isEmpty()) {
			return Lists.newArrayList();
		}
		Example e = new Example(ImageCert.class);
		e.and().andIn("billId", billIdList);
		return this.getDao().selectByExample(e);
	}

	public List<ImageCert> insertImageCert(List<ImageCert> imageCertList, Long billId, Integer billType) {
		if (billId != null) {
			// 先删除全部原有图片
			ImageCert deleteConditon = new ImageCert();
			deleteConditon.setBillId(billId);
			deleteConditon.setBillType(billType);
			this.deleteByExample(deleteConditon);

			// 增加新的图片
			return StreamEx.of(imageCertList).nonNull().map(cert -> {
				cert.setBillId(billId);
				cert.setId(null);
				cert.setBillType(billType);
				this.insertSelective(cert);
				return cert;
			}).toList();
		}
		return Lists.newArrayList();
	}

	public List<ImageCert> findImageCertListByBillId(Long billId, Integer billType) {
		if (billId == null) {
			return Lists.newArrayList();
		}
		ImageCert queryCondition = new ImageCert();
		queryCondition.setBillId(billId);
		queryCondition.setBillType(billType);
		return this.listByExample(queryCondition);
	}
}
