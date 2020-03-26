package com.dili.trace.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.ApproverInfo;
import com.dili.trace.domain.Base64Signature;
import com.dili.trace.service.ApproverInfoService;
import com.dili.trace.service.Base64SignatureService;

@Service
public class ApproverInfoServiceImpl extends BaseServiceImpl<ApproverInfo, Long> implements ApproverInfoService {
	private static final int MAX_LENGTH=1000;

	@Autowired
	Base64SignatureService base64SignatureService;

	@Transactional
	@Override
	public int insertApproverInfo(ApproverInfo input) {

		String signBase64 = input.getSignBase64();
		if (StringUtils.isBlank(signBase64)) {
			throw new BusinessException("签名不能为空");
		}
		this.insertSelective(input);


		List<Base64Signature> base64SignatureList = new ArrayList<>();

		for (int start = 0; start < signBase64.length(); start += MAX_LENGTH) {
			String base64 = signBase64.substring(start, Math.min(signBase64.length(), start + MAX_LENGTH));
			Base64Signature base64Signature = DTOUtils.newDTO(Base64Signature.class);
			base64Signature.setBase64(base64);
			base64Signature.setApproverInfoId(input.getId());
			base64Signature.setOrderNum(base64SignatureList.size());
			base64SignatureList.add(base64Signature);
		}

		base64SignatureService.batchInsert(base64SignatureList);
		return 1;
	}
	@Transactional
	@Override
	public int updateApproverInfo(ApproverInfo input) {

		String signBase64 = input.getSignBase64();
		if (StringUtils.isBlank(signBase64)) {
			throw new BusinessException("签名不能为空");
		}
		ApproverInfo approverInfo=this.get(input.getId());
		approverInfo.setUserName(input.getUserName());
		approverInfo.setUserId(input.getUserId());
		approverInfo.setPhone(input.getPhone());
		
		this.updateSelective(approverInfo);
		String oldSignBase64=this.base64SignatureService.findBase64SignatureByApproverInfoId(approverInfo.getId());
		
		if(!signBase64.equals(oldSignBase64)) {
			Base64Signature condition=DTOUtils.newDTO(Base64Signature.class);
			condition.setApproverInfoId(approverInfo.getId());
			this.base64SignatureService.deleteByExample(condition);

			List<Base64Signature> base64SignatureList = new ArrayList<>();

			for (int start = 0; start < signBase64.length(); start += MAX_LENGTH) {
				String base64 = signBase64.substring(start, Math.min(signBase64.length(), start + MAX_LENGTH));
				Base64Signature base64Signature = DTOUtils.newDTO(Base64Signature.class);
				base64Signature.setBase64(base64);
				base64Signature.setApproverInfoId(approverInfo.getId());
				base64Signature.setOrderNum(base64SignatureList.size());
				base64SignatureList.add(base64Signature);
			}

			base64SignatureService.batchInsert(base64SignatureList);
		}
		

		return 1;
	}

	

}