package com.dili.trace.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.dto.CheckOutApiInput;
import com.dili.trace.api.dto.CheckoutApiDetailOutput;
import com.dili.trace.domain.CheckoutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.glossary.CheckoutStatusEnum;

@Service
public class CheckoutRecordService extends BaseServiceImpl<CheckoutRecord, Long> {
	@Autowired
	UserService userService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	UpStreamService upStreamService;

	@Transactional
	public CheckoutRecord doCheckout(OperatorUser operateUser, CheckOutApiInput checkOutApiInput) {
		if (checkOutApiInput == null || checkOutApiInput.getSeparateSalesIdList() == null
				|| checkOutApiInput.getCheckoutStatus() == null) {
			throw new BusinessException("参数错误");
		}
		CheckoutStatusEnum checkoutStatusEnum = CheckoutStatusEnum.fromCode(checkOutApiInput.getCheckoutStatus());

		if (checkoutStatusEnum == null) {
			throw new BusinessException("参数错误");
		}
		List<SeparateSalesRecord> recordList = checkOutApiInput.getSeparateSalesIdList().stream().map(billId -> {

			SeparateSalesRecord record = this.separateSalesRecordService.get(billId);
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

		// User user = this.userService.get(checkInApiInput.getUserId());
		// if (user == null) {
		// throw new BusinessException("数据错误");
		// }

		CheckoutRecord checkoutRecord = new CheckoutRecord();
		checkoutRecord.setCheckoutStatus(checkoutStatusEnum.getCode());
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

	public CheckoutApiDetailOutput getCheckoutDataDetail(Long separateSalesId) {
		if (separateSalesId == null) {
			throw new BusinessException("参数错误");
		}
		SeparateSalesRecord separateSalesRecord = this.separateSalesRecordService.get(separateSalesId);
		if(separateSalesRecord==null) {
			throw new BusinessException("没有数据");
		}
		CheckoutApiDetailOutput output=new CheckoutApiDetailOutput();
		
		RegisterBill bill=this.registerBillService.get(separateSalesRecord.getBillId());
		User user=this.userService.get(separateSalesRecord.getSalesUserId());
		output.setId(separateSalesId);
		output.setState(bill.getState());
		output.setUser(user);
		if(separateSalesRecord.getParentId()==null&&bill.getUpStreamId()!=null) {
			UpStream upStream=this.upStreamService.get(bill.getUpStreamId());
			output.setUpStream(upStream);
		}else if(separateSalesRecord.getParentId()!=null) {
			SeparateSalesRecord parentSalesRecord = this.separateSalesRecordService.get(separateSalesRecord.getParentId());
			if(parentSalesRecord!=null&&parentSalesRecord.getSalesUserId()!=null) {
				UpStream query=new UpStream();
				query.setSourceUserId(parentSalesRecord.getSalesUserId());
				UpStream upStream=this.upStreamService.listByExample(query).stream().findFirst().orElse(null);
				output.setUpStream(upStream);
			}
		
		}
		
		return output;

	}

}