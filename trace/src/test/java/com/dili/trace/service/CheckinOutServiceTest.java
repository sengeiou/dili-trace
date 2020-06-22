package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.IDTO;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.api.dto.CheckInApiListOutput;
import com.dili.trace.api.dto.CheckOutApiInput;
import com.dili.trace.api.dto.CheckoutApiListQuery;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.CheckinOutTypeEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.CheckoutStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.google.common.collect.Lists;

@TestInstance(Lifecycle.PER_CLASS)
public class CheckinOutServiceTest extends AutoWiredBaseTest {
	@Autowired
	CheckinOutRecordService checkinOutRecordService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	TradeDetailService tradeDetailService;
	@Test
	public void listCheckInApiListOutputPage() {
		RegisterBillDto query = new RegisterBillDto();
		query.setUserId(1L);
		query.setPage(1);
		query.setRows(20);
		BasePage<CheckInApiListOutput> out = this.checkinOutRecordService
				.listCheckInApiListOutputPage(new RegisterBillDto());
		System.out.println(out);
	}
//	@BeforeAll
//	public void startTransaction() {
//		assertFalse(TestTransaction.isActive());
//		TransactionContext transactionContext
//		  = TransactionContextHolder.getCurrentTransactionContext();
//		
//		TestTransaction.start();
//		TestTransaction.flagForRollback();
//		assertTrue(TestTransaction.isActive());
//		assertTrue(TestTransaction.isFlaggedForRollback());
//	}c2020062000001
//
//	@AfterAll
//	public void endTransaction() {
//		TestTransaction.end();
//	}
	

	@Test
	public void doCheckin() {
		RegisterBill query = new RegisterBill();
		query.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());

		query.setMetadata(IDTO.AND_CONDITION_EXPR,
				"id in (select bill_id from separate_sales_record where checkin_record_id is null and `sales_type` = 0)");

		List<Long> billIdList = this.registerBillService.listByExample(query).stream().map(RegisterBill::getId).limit(1)
				.collect(Collectors.toList());
		CheckInApiInput checkInApiInput = new CheckInApiInput();
		checkInApiInput.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
		checkInApiInput.setBillIdList(billIdList);
		List<CheckinOutRecord> record = this.checkinOutRecordService.doCheckin(new OperatorUser(1111L, "wangguofeng"),
				checkInApiInput);
		System.out.println(record.size());
	}

	@Test
	public void doManullyCheck() {
		RegisterBillDto query = new RegisterBillDto();
		query.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
		query.setMetadata(IDTO.AND_CONDITION_EXPR,
				"id in (select bill_id from separate_sales_record where checkin_record_id is not null and checkout_record_id is null and `sales_type` = 0)");

		List<Long> billIdList = this.registerBillService.listByExample(query).stream().map(RegisterBill::getId)
				.flatMap(billId -> {
					SeparateSalesRecord q = new SeparateSalesRecord();
					q.setBillId(billId);
					return this.separateSalesRecordService.listByExample(q).stream();

				}).map(SeparateSalesRecord::getBillId).filter(Objects::nonNull).limit(1).collect(Collectors.toList());
		if (billIdList.size() == 0) {
			throw new RuntimeException("没有数据可以测试");
		}
//		ManullyCheckInput input = new ManullyCheckInput();
//		input.setPass(true);
//		input.setBillId(billIdList.get(0));
//		this.checkinOutRecordService.doManullyCheck(new OperatorUser(2222L, "wangguofeng"), input);
	}

	@Test
	public void doCheckout() {
		RegisterBillDto query = new RegisterBillDto();
		query.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
		query.setDetectStateList(
				Arrays.asList(BillDetectStateEnum.PASS.getCode(), BillDetectStateEnum.REVIEW_PASS.getCode()));
		query.setMetadata(IDTO.AND_CONDITION_EXPR,
				"id in (select bill_id from separate_sales_record where checkin_record_id is not null  and checkout_record_id is null  and `sales_type` = 0)");

		List<Long> separateSalesIdList = this.registerBillService.listByExample(query).stream().map(RegisterBill::getId)
				.flatMap(billId -> {
					SeparateSalesRecord q = new SeparateSalesRecord();
					q.setBillId(billId);
					return this.separateSalesRecordService.listByExample(q).stream();

				}).map(SeparateSalesRecord::getId).filter(Objects::nonNull).limit(1).collect(Collectors.toList());
		CheckOutApiInput checkInApiInput = new CheckOutApiInput();
		checkInApiInput.setCheckoutStatus(CheckoutStatusEnum.ALLOWED.getCode());
//		checkInApiInput.setSeparateSalesIdList(separateSalesIdList);
		List<CheckinOutRecord> record = this.checkinOutRecordService.doCheckout(new OperatorUser(3333L, "wangguofeng"),
				checkInApiInput);
		System.out.println(record.size());
	}

	@Test
	public void listPagedAvailableCheckOutData() {
		SeparateSalesRecord domain = new SeparateSalesRecord();
		domain.setMetadata(IDTO.AND_CONDITION_EXPR,
				"checkin_record_id in(\n" + "		select\n" + "			id\n" + "		from\n"
						+ "			checkinout_record\n" + "		where\n" + "			`inout` = 10\n"
						+ "			and status = 10)\n" + "		and checkout_record_id is null");

		Long userId = this.separateSalesRecordService.listByExample(domain).stream()
				.map(SeparateSalesRecord::getSalesUserId).findFirst().orElse(null);
		if (userId == null) {
			throw new RuntimeException("没有可以查询的数据");
		}
		CheckoutApiListQuery query = new CheckoutApiListQuery();
		query.setUserId(userId);

		BaseOutput<BasePage<DTO>> out = this.checkinOutRecordService.listPagedAvailableCheckOutData(query);
		System.out.println(JSON.toJSONString(out));
	}

	@Test
	public void listPagedAvailableCheckOutData2() {
		CheckoutApiListQuery query = new CheckoutApiListQuery();
		query.setUserId(1L);
		query.setLikeProductName("abc");
		BaseOutput<BasePage<DTO>> out = this.checkinOutRecordService.listPagedAvailableCheckOutData(query);
		out.getData().getDatas();
	}

	@Test
	public void listPagedData() {
		CheckoutApiListQuery query = new CheckoutApiListQuery();
		query.setDate("2020-05-28");
		BaseOutput<BasePage<Map<String, Object>>> out = this.checkinOutRecordService.listPagedData(query, 496L);
		System.out.println(out.getData().getDatas());
	}


	
	@Test
	@Transactional
	public void testAll() {
		RegisterBill query = new RegisterBill();
		query.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
		RegisterBill item = this.registerBillService.listByExample(query).stream().findFirst().orElse(null);
		assertNotNull(item);
		item.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
		Long billId = this.registerBillService.doVerify(item);
		assertNotNull(billId);
		assertTrue(BillVerifyStatusEnum.PASSED.equalsToCode(this.registerBillService.get(billId).getVerifyStatus()));

		TradeDetail sepQuery = new TradeDetail();
		sepQuery.setBillId(billId);
		sepQuery.setTradeType(TradeTypeEnum.NONE.getCode());

		TradeDetail tradeDetailItem = this.tradeDetailService.listByExample(sepQuery).stream()
				.findFirst().orElse(null);
		assertNotNull(tradeDetailItem);

		assertEquals(tradeDetailItem.getCheckinStatus(), CheckinStatusEnum.NONE.getCode());
		assertEquals(tradeDetailItem.getCheckoutStatus(), CheckoutStatusEnum.NONE.getCode());
		assertEquals(tradeDetailItem.getTradeType(), TradeTypeEnum.NONE.getCode());
		assertEquals(tradeDetailItem.getSaleStatus(), SaleStatusEnum.NONE.getCode());

		// ------doCheckin--------
		CheckInApiInput checkInApiInput = new CheckInApiInput();
		checkInApiInput.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
		checkInApiInput.setBillIdList(Lists.newArrayList(billId));
		List<CheckinOutRecord> inoutList = this.checkinOutRecordService.doCheckin(new OperatorUser(1L, ""),
				checkInApiInput);
		assertEquals(inoutList.size(), 1);
		CheckinOutRecord inRecord = inoutList.get(0);

		tradeDetailItem = this.tradeDetailService.listByExample(sepQuery).stream().findFirst().orElse(null);
		assertNotNull(tradeDetailItem);
		assertEquals(tradeDetailItem.getCheckinStatus(), CheckinStatusEnum.ALLOWED.getCode());
		assertEquals(tradeDetailItem.getCheckoutStatus(), CheckoutStatusEnum.NONE.getCode());
		assertEquals(tradeDetailItem.getTradeType(), TradeTypeEnum.NONE.getCode());
		assertEquals(tradeDetailItem.getSaleStatus(), SaleStatusEnum.FOR_SALE.getCode());
		assertEquals(tradeDetailItem.getCheckinRecordId(), inRecord.getId());

		assertEquals(inRecord.getInout(), CheckinOutTypeEnum.IN.getCode());
		assertEquals(inRecord.getStatus(), CheckinStatusEnum.ALLOWED.getCode());
		assertEquals(inRecord.getTradeDetailId(), tradeDetailItem.getId());
//
//		// ------doManullyCheck--------
//		ManullyCheckInput inputCheckInput = new ManullyCheckInput();
//		inputCheckInput.setBillId(billId);
//		inputCheckInput.setPass(true);
//		this.checkinOutRecordService.doManullyCheck(new OperatorUser(1L, ""), inputCheckInput);
		tradeDetailItem = this.tradeDetailService.listByExample(sepQuery).stream().findFirst().orElse(null);

		// ====================
		CheckOutApiInput checkOutApiInput = new CheckOutApiInput();
		checkOutApiInput.setCheckoutStatus(CheckoutStatusEnum.ALLOWED.getCode());
		checkOutApiInput.setTradeDetailIdList(Lists.newArrayList(tradeDetailItem.getId()));
		List<CheckinOutRecord> outlist = this.checkinOutRecordService.doCheckout(new OperatorUser(1L, ""),
				checkOutApiInput);
		assertEquals(outlist.size(), 1);
		CheckinOutRecord outrecord = outlist.get(0);
		assertEquals(outrecord.getInout(), CheckinOutTypeEnum.OUT.getCode());
		assertEquals(outrecord.getStatus(), CheckoutStatusEnum.ALLOWED.getCode());
		assertEquals(outrecord.getTradeDetailId(), tradeDetailItem.getId());

	}

}
