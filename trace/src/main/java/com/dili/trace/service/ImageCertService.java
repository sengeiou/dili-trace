package com.dili.trace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.ImageCert;

import one.util.streamex.StreamEx;

@Service
public class ImageCertService extends BaseServiceImpl<ImageCert, Long> {
	public List<ImageCert> insertImageCert(List<ImageCert> imageCertList, Long billId) {
		return StreamEx.of(imageCertList).nonNull().map(cert -> {
			cert.setTargetId(billId);
			this.insertSelective(cert);
			return cert;
		}).toList();
	}

}
