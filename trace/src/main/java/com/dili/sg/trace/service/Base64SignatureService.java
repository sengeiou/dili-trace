package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.Base64Signature;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface Base64SignatureService extends BaseService<Base64Signature, Long> {
	public String findBase64SignatureByApproverInfoId(Long approverId);

}