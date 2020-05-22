package com.dili.trace.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.api.dto.CheckOutApiInput;
import com.dili.trace.api.dto.ManullyCheckInput;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.CheckoutStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;

public class CheckinOutServiceTest extends AutoWiredBaseTest {
	@Autowired
	CheckinOutRecordService checkinOutRecordService;
	@Autowired
	RegisterBillService begisterBillService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;

	@Test
	public void listCheckInApiListOutputPage() {
		this.checkinOutRecordService.listCheckInApiListOutputPage(DTOUtils.newDTO(RegisterBill.class));
	}

	@Test
	public void doCheckin() {
		RegisterBill query = DTOUtils.newDTO(RegisterBill.class);
		query.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
		
		query.mset(IDTO.AND_CONDITION_EXPR, "id in (select bill_id from separate_sales_record where checkin_record_id is null and `sales_type` = 0)");
		
		List<Long> billIdList = this.begisterBillService.listByExample(query).stream()
				.map(RegisterBill::getId).limit(1).collect(Collectors.toList());
		CheckInApiInput checkInApiInput = new CheckInApiInput();
		checkInApiInput.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
		checkInApiInput.setBillIdList(billIdList);
		CheckinOutRecord record=this.checkinOutRecordService.doCheckin(new OperatorUser(1111L, "wangguofeng"), checkInApiInput);
		System.out.println(record.getId());
	}
	@Test
	public void doManullyCheck() {
		RegisterBillDto query = DTOUtils.newDTO(RegisterBillDto.class);
		query.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
		query.mset(IDTO.AND_CONDITION_EXPR, "id in (select bill_id from separate_sales_record where checkin_record_id is not null and checkout_record_id is null and `sales_type` = 0)");
		
		List<Long> separateSalesIdList = this.begisterBillService.listByExample(query).stream()
				.map(RegisterBill::getId).flatMap(billId->{
					SeparateSalesRecord q=DTOUtils.newDTO(SeparateSalesRecord.class);
					q.setBillId(billId);
					return this.separateSalesRecordService.listByExample(q).stream();
					
					
				}).map(SeparateSalesRecord::getId).filter(Objects::nonNull).limit(1).collect(Collectors.toList());
		if(separateSalesIdList.size()==0) {
			throw new RuntimeException("没有数据可以测试");
		}
		ManullyCheckInput input=new ManullyCheckInput();
		input.setPass(true);
		input.setSeparateSalesId(separateSalesIdList.get(0));
		this.checkinOutRecordService.doManullyCheck(new OperatorUser(2222L, "wangguofeng"),input);
	}
	@Test
	public void doCheckout() {
		RegisterBillDto query = DTOUtils.newDTO(RegisterBillDto.class);
		query.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
		query.setDetectStateList(Arrays.asList(BillDetectStateEnum.PASS.getCode(),BillDetectStateEnum.REVIEW_PASS.getCode()));
		query.mset(IDTO.AND_CONDITION_EXPR, "id in (select bill_id from separate_sales_record where checkin_record_id is not null  and checkout_record_id is null  and `sales_type` = 0)");
		
		List<Long> separateSalesIdList = this.begisterBillService.listByExample(query).stream()
				.map(RegisterBill::getId).flatMap(billId->{
					SeparateSalesRecord q=DTOUtils.newDTO(SeparateSalesRecord.class);
					q.setBillId(billId);
					return this.separateSalesRecordService.listByExample(q).stream();
					
					
				}).map(SeparateSalesRecord::getId).filter(Objects::nonNull).limit(1).collect(Collectors.toList());
		CheckOutApiInput checkInApiInput = new CheckOutApiInput();
		checkInApiInput.setCheckoutStatus(CheckoutStatusEnum.ALLOWED.getCode());
		checkInApiInput.setSeparateSalesIdList(separateSalesIdList);
		CheckinOutRecord record=this.checkinOutRecordService.doCheckout(new OperatorUser(3333L, "wangguofeng"), checkInApiInput);
		System.out.println(record.getId());
	}


}