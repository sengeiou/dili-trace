package com.dili.trace.service;

import java.util.ArrayList;
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
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.dto.CheckInApiDetailOutput;
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.api.dto.CheckInApiListOutput;
import com.dili.trace.api.dto.CheckOutApiInput;
import com.dili.trace.api.dto.CheckoutApiDetailOutput;
import com.dili.trace.api.dto.ManullyCheckInput;
import com.dili.trace.dao.CheckinOutRecordMapper;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.CheckinOutTypeEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.CheckoutStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.github.pagehelper.Page;

@Service
public class CheckinOutRecordService extends BaseServiceImpl<CheckinOutRecord, Long> {
	@Autowired
	UserService userService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	CheckinOutRecordMapper checkinOutRecordMapper;

	@Transactional
	public CheckinOutRecord doCheckout(OperatorUser operateUser, CheckOutApiInput checkOutApiInput) {
		if (checkOutApiInput == null || checkOutApiInput.getSeparateSalesIdList() == null
				|| checkOutApiInput.getCheckoutStatus() == null) {
			throw new BusinessException("参数错误");
		}
		CheckoutStatusEnum checkoutStatusEnum = CheckoutStatusEnum.fromCode(checkOutApiInput.getCheckoutStatus());

		if (checkoutStatusEnum == null) {
			throw new BusinessException("参数错误");
		}
		List<SeparateSalesRecord> recordList = checkOutApiInput.getSeparateSalesIdList().stream().map(id -> {

			SeparateSalesRecord record = this.separateSalesRecordService.get(id);
			if (record == null) {
				return null;
			} else {
				if (record.getCheckoutRecordId() == null) {
					return record;
				}
			}

			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
		if (recordList.isEmpty()) {
			throw new BusinessException("没有可以出场的交易单");
		}
		List<Long> billList = recordList.stream().map(SeparateSalesRecord::getBillId).filter(billid -> {

			RegisterBill registerBill = this.registerBillService.get(billid);
			if (registerBill == null) {
				return false;
			}
			if (!RegisterBillStateEnum.ALREADY_AUDIT.getCode().equals(registerBill.getState())) {
				return false;
			}
			if (BillDetectStateEnum.PASS.getCode().equals(registerBill.getDetectState())
					|| BillDetectStateEnum.REVIEW_PASS.getCode().equals(registerBill.getDetectState())) {
				return true;
			}
			return false;

		}).collect(Collectors.toList());

		if (billList.size() != checkOutApiInput.getSeparateSalesIdList().size()) {
			throw new BusinessException("部分交易单不能出门，请重新确认");
		}
		CheckinOutRecord checkoutRecord = new CheckinOutRecord();
		checkoutRecord.setStatus(checkoutStatusEnum.getCode());
		checkoutRecord.setInout(CheckinOutTypeEnum.OUT.getCode());
		checkoutRecord.setOperatorId(operateUser.getId());
		checkoutRecord.setOperatorName(operateUser.getName());
		checkoutRecord.setRemark(checkOutApiInput.getRemark());
		checkoutRecord.setCreated(new Date());
		checkoutRecord.setModified(new Date());
		int returnValue = this.insertSelective(checkoutRecord);
		recordList.stream().forEach(record -> {
			SeparateSalesRecord updatable = DTOUtils.newDTO(SeparateSalesRecord.class);
			updatable.setId(record.getId());
			updatable.setCheckoutRecordId(checkoutRecord.getId());
			this.separateSalesRecordService.updateSelective(updatable);
		});

		return checkoutRecord;

	}

	@Transactional
	public CheckinOutRecord doCheckin(OperatorUser operateUser, CheckInApiInput checkInApiInput) {
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

		CheckinOutRecord checkinRecord = new CheckinOutRecord();
		checkinRecord.setStatus(checkinStatusEnum.getCode());
		checkinRecord.setInout(CheckinOutTypeEnum.IN.getCode());
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

	@Transactional
	public SeparateSalesRecord doManullyCheck(OperatorUser operateUser, ManullyCheckInput input) {
		if (input == null || input.getSeparateSalesId() == null || input.getPass() == null) {
			throw new BusinessException("参数错误");
		}

		SeparateSalesRecord separateSalesRecord = this.separateSalesRecordService.get(input.getSeparateSalesId());

		if (separateSalesRecord == null) {
			throw new BusinessException("数据错误:没有数据");
		}
		if (separateSalesRecord.getCheckoutRecordId() != null) {
			throw new BusinessException("数据错误:已经出门");
		}
		CheckinOutRecord checkinRecord = this.get(separateSalesRecord.getCheckinRecordId());

		if (checkinRecord == null || CheckinOutTypeEnum.IN != CheckinOutTypeEnum.fromCode(checkinRecord.getInout())) {
			throw new BusinessException("数据错误:没有进门数据");
		}

		CheckinStatusEnum checkinStatusEnum = CheckinStatusEnum.fromCode(checkinRecord.getStatus());

		if (CheckinStatusEnum.ALLOWED != checkinStatusEnum) {
			throw new BusinessException("数据错误:进门状态错误");
		}
		RegisterBill bill = this.registerBillService.get(separateSalesRecord.getBillId());
		if (bill == null) {
			throw new BusinessException("没有找到登记单");
		}
		if (!RegisterBillStateEnum.WAIT_CHECK.getCode().equals(bill.getState())) {
			throw new BusinessException("登记单状态错误");
		}

		RegisterBill updatable = DTOUtils.newDTO(RegisterBill.class);
		updatable.setId(bill.getId());
		updatable.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
		if (input.getPass()) {
			if (bill.getDetectState() != null) {
				updatable.setDetectState(BillDetectStateEnum.REVIEW_PASS.getCode());
			} else {
				updatable.setDetectState(BillDetectStateEnum.PASS.getCode());
			}
			updatable.setLatestPdResult("合格");
		} else {
			if (bill.getDetectState() != null) {
				updatable.setDetectState(BillDetectStateEnum.REVIEW_NO_PASS.getCode());
			} else {
				updatable.setDetectState(BillDetectStateEnum.NO_PASS.getCode());
			}
			updatable.setLatestPdResult("不合格");
		}
		updatable.setLatestDetectTime(new Date());
		updatable.setLatestDetectOperator("管理员");

		this.registerBillService.updateSelective(updatable);
		return separateSalesRecord;

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
			output.setState(registerBill.getState());
			return Optional.of(output);
		}
		return Optional.empty();
	}

	public BasePage<CheckInApiListOutput> listCheckInApiListOutputPage(RegisterBill query) {

		RegisterBill condition = DTOUtils.newDTO(RegisterBill.class);
		List<String> sqlList = new ArrayList<>();

		if (query.getUserId() != null) {

			sqlList.add("user_id=" + query.getUserId());

		}
		if (query.getUpStreamId() != null) {

			sqlList.add("upstream_id=" + query.getUpStreamId());

		}
		if (query.getState() != null) {

			sqlList.add("state=" + query.getState());
		}
		if (sqlList.size() > 0) {

		}
		condition.mset(IDTO.AND_CONDITION_EXPR, String.join("AND ", sqlList));

		BasePage<RegisterBill> billPage = this.registerBillService.listPageByExample(condition);
		List<CheckInApiListOutput> data = billPage.getDatas().stream().map(bill -> {
			CheckInApiListOutput out = new CheckInApiListOutput();
			out.setId(bill.getId());
			out.setCode(bill.getCode());
			out.setName(bill.getName());
			out.setPhone(bill.getPhone());
			out.setState(bill.getState());
			Optional.ofNullable(this.upStreamService.get(bill.getUpStreamId())).ifPresent(up -> {

				out.setUpstreamName(up.getName());
				out.setUpstreamTelphone(up.getTelphone());
			});
			;

			return out;
		}).collect(Collectors.toList());

		BasePage<CheckInApiListOutput> result = new BasePage<>();
		result.setDatas(data);
		result.setPage(billPage.getPage());
		result.setRows(billPage.getRows());
		result.setTotalItem(billPage.getTotalItem());
		result.setTotalPage(billPage.getTotalPage());
		result.setStartIndex(billPage.getStartIndex());

		return result;

	}

	public CheckoutApiDetailOutput getCheckoutDataDetail(Long separateSalesId) {
		if (separateSalesId == null) {
			throw new BusinessException("参数错误");
		}
		SeparateSalesRecord separateSalesRecord = this.separateSalesRecordService.get(separateSalesId);
		if (separateSalesRecord == null) {
			throw new BusinessException("没有数据");
		}
		CheckoutApiDetailOutput output = new CheckoutApiDetailOutput();

		RegisterBill bill = this.registerBillService.get(separateSalesRecord.getBillId());
		User user = this.userService.get(separateSalesRecord.getSalesUserId());
		output.setId(separateSalesId);
		output.setState(bill.getState());
		output.setUser(user);
		if (separateSalesRecord.getParentId() == null && bill.getUpStreamId() != null) {
			UpStream upStream = this.upStreamService.get(bill.getUpStreamId());
			output.setUpStream(upStream);
		} else if (separateSalesRecord.getParentId() != null) {
			SeparateSalesRecord parentSalesRecord = this.separateSalesRecordService
					.get(separateSalesRecord.getParentId());
			if (parentSalesRecord != null && parentSalesRecord.getSalesUserId() != null) {
				UpStream upStream = this.upStreamService
						.queryUpStreamBySourceUserId(parentSalesRecord.getSalesUserId());
				output.setUpStream(upStream);
			}

		}

		return output;

	}

}