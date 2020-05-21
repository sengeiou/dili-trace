package com.dili.trace.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.dto.CheckInApiDetailOutput;
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.api.dto.CheckInApiListOutput;
import com.dili.trace.dao.CheckinRecordMapper;
import com.dili.trace.domain.CheckinRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;

@Service
public class CheckinRecordService extends BaseServiceImpl<CheckinRecord, Long> {
	@Autowired
	UserService userService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	CheckinRecordMapper checkinRecordMapper;
	@Autowired
	UpStreamService upStreamService;

	@Transactional
	public CheckinRecord doCheckin(OperatorUser operateUser, CheckInApiInput checkInApiInput) {
		if (checkInApiInput == null || checkInApiInput.getBillIdList() == null
				|| checkInApiInput.getCheckinStatus() == null) {
			throw new BusinessException("参数错误");
		}
		CheckinStatusEnum checkinStatusEnum = CheckinStatusEnum.fromCode(checkInApiInput.getCheckinStatus());

		if (checkinStatusEnum == null) {
			throw new BusinessException("参数错误");
		}
		List<RegisterBill> billList = checkInApiInput.getBillIdList().stream().map(billId -> {

			RegisterBill bill = this.registerBillService.get(billId);
			if (bill == null) {
				return null;
			} else {
				if (RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(bill.getState())) {
					return bill;
				}
			}

			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
		if (billList.isEmpty()) {
			throw new BusinessException("没有可以进场的登记单");
		}

		// User user = this.userService.get(checkInApiInput.getUserId());
		// if (user == null) {
		// throw new BusinessException("数据错误");
		// }

		CheckinRecord checkinRecord = new CheckinRecord();
		checkinRecord.setCheckinStatus(checkinStatusEnum.getCode());
		checkinRecord.setOperatorId(operateUser.getId());
		checkinRecord.setOperatorName(operateUser.getName());
		checkinRecord.setRemark(checkInApiInput.getRemark());
		checkinRecord.setCreated(new Date());
		checkinRecord.setModified(new Date());
		int returnValue = this.insertSelective(checkinRecord);
		billList.stream().forEach(bill -> {
			this.separateSalesRecordService.checkInSeparateSalesRecord(checkinRecord.getId(), bill.getId());
			RegisterBill updatable = DTOUtils.newDTO(RegisterBill.class);
			updatable.setId(bill.getId());
			if (CheckinStatusEnum.ALLOWED == checkinStatusEnum) {
				updatable.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
			} else {
				updatable.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
				updatable.setDetectState(BillDetectStateEnum.NO_PASS.getCode());
			}

			this.registerBillService.updateSelective(updatable);
		});

		return checkinRecord;

	}

	public Optional<CheckInApiDetailOutput> getCheckInDetail(Long billId) {
		RegisterBill registerBill = this.registerBillService.get(billId);
		if (registerBill != null) {
			CheckInApiDetailOutput output = new CheckInApiDetailOutput();
			Long upstreamId = registerBill.getUpStreamId();
			Long userId = registerBill.getUserId();
			if (upstreamId != null) {
				UpStream upStream = this.upStreamService.get(upstreamId);
				output.setUpStream(upStream);
			}
			if (userId != null) {
				User user = this.userService.get(userId);
				output.setUser(user);
			}
			output.setId(registerBill.getId());
			output.setCode(registerBill.getCode());
			output.setName(registerBill.getName());
			output.setPhone(registerBill.getPhone());
			output.setState(registerBill.getState());
			return Optional.of(output);
		}
		return Optional.empty();
	}

	public BasePage<CheckInApiListOutput> listCheckInApiListOutputPage(RegisterBill query) {
		query.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
		Integer total = this.checkinRecordMapper.countlistCheckInRecord(query);
		List<CheckInApiListOutput> list = this.checkinRecordMapper.listCheckInRecord(query);
		BasePage<CheckInApiListOutput> result = new BasePage();

		result.setPage(query.getPage());
		result.setRows(query.getRows());
		result.setTotalItem(total);

		return result;

	}

}