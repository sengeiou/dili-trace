package com.dili.trace.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dili.common.exception.TraceBusinessException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.VerifyHistory;
import com.dili.trace.dto.OperatorUser;

import one.util.streamex.StreamEx;

@Service
public class VerifyHistoryService extends BaseServiceImpl<VerifyHistory, Long> {
	public VerifyHistory insertVerifyHistory(VerifyHistory verifyHistory, OperatorUser operatorUser) {
		if (verifyHistory == null || verifyHistory.getBillId() == null || verifyHistory.getToVerifyStatus() == null
				|| operatorUser == null) {
			throw new TraceBusinessException("参数错误");
		}
		this.invalidVerifyHistory(verifyHistory.getBillId());

		verifyHistory.setVerifyUserId(operatorUser.getId());
		verifyHistory.setVerifyUserName(operatorUser.getName());
		verifyHistory.setCreated(new Date());
		verifyHistory.setModified(new Date());
		verifyHistory.setValid(YesOrNoEnum.YES.getCode());

		this.insertSelective(verifyHistory);
		return verifyHistory;

	}

	public VerifyHistory insertVerifyHistory(Long billId, Integer fromVerifyStatus, Integer toVerifyStatus,
			OperatorUser operatorUser) {
		VerifyHistory verifyHistory = new VerifyHistory();
		verifyHistory.setBillId(billId);
		verifyHistory.setFromVerifyStatus(fromVerifyStatus);
		verifyHistory.setToVerifyStatus(toVerifyStatus);

		return this.insertVerifyHistory(verifyHistory, operatorUser);

	}

	private void invalidVerifyHistory(Long billId) {
		VerifyHistory updateCondition = new VerifyHistory();
		updateCondition.setBillId(billId);
		updateCondition.setValid(YesOrNoEnum.YES.getCode());

		VerifyHistory domain = new VerifyHistory();
		domain.setValid(YesOrNoEnum.NO.getCode());
		domain.setModified(new Date());
		this.updateSelectiveByExample(domain, updateCondition);
	}

	public Optional<VerifyHistory> findValidVerifyHistoryByBillId(Long billId) {

		VerifyHistory query = new VerifyHistory();
		query.setBillId(billId);
		query.setValid(YesOrNoEnum.YES.getCode());
		return StreamEx.of(this.listByExample(query)).findFirst();

	}

}
