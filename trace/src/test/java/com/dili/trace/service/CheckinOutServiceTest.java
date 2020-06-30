package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.IDTO;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.CheckInApiInput;
import com.dili.trace.api.input.CheckOutApiInput;
import com.dili.trace.api.output.CheckInApiListOutput;
import com.dili.trace.api.output.CheckoutApiListQuery;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinOutTypeEnum;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.CheckoutStatusEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.enums.VerifyTypeEnum;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

	// @Test
	public void listCheckInApiListOutputPage() {
		RegisterBillDto query = new RegisterBillDto();
		query.setUserId(1L);
		query.setPage(1);
		query.setRows(20);
		BasePage<CheckInApiListOutput> out = this.checkinOutRecordService
				.listCheckInApiListOutputPage(new RegisterBillDto());
		System.out.println(out);
	}

	// @Test
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

	// @Test
	public void listPagedAvailableCheckOutData2() {
		CheckoutApiListQuery query = new CheckoutApiListQuery();
		query.setUserId(1L);
		query.setLikeProductName("abc");
		BaseOutput<BasePage<DTO>> out = this.checkinOutRecordService.listPagedAvailableCheckOutData(query);
		out.getData().getDatas();
	}

	@Test
	@Transactional
	public void doCheckinBeforeVerify() {
		// ------createRegisterBill--------
		Long billId = super.createRegisterBill(super.buildBill()).getId();
		assertNotNull(billId);
		// ------doCheckIn--------
		Pair<CheckinOutRecord, TradeDetail> pair = super.doCheckIn(billId, CheckinStatusEnum.ALLOWED);
		CheckinOutRecord inRecord = pair.getLeft();
		TradeDetail tradeDetailItem = pair.getRight();
		assertEquals(tradeDetailItem.getSaleStatus(), SaleStatusEnum.NONE.getCode());
		// ------doVerifyAfterCheckIn--------
		super.doVerifyAfterCheckIn(billId, BillVerifyStatusEnum.PASSED);

		TradeDetail sepQuery = new TradeDetail();
		sepQuery.setBillId(billId);
		sepQuery.setTradeType(TradeTypeEnum.NONE.getCode());
		TradeDetail tradeDetail = this.tradeDetailService.listByExample(sepQuery).stream().findFirst().orElse(null);
		assertNotNull(tradeDetail);
		assertEquals(tradeDetail.getSaleStatus(), SaleStatusEnum.FOR_SALE.getCode());
		assertEquals(tradeDetail.getCheckinRecordId(), inRecord.getId());
		assertEquals(inRecord.getTradeDetailId(), tradeDetail.getId());

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

	@Test
	@Transactional
	public void doCheckinAferVerify() {
		// ------doVerifyBeforeCheckIn--------
		Long billId = super.createRegisterBill(super.buildBill()).getId();
		// ------doVerifyBeforeCheckIn--------
		super.doVerifyBeforeCheckIn(billId, BillVerifyStatusEnum.PASSED);

		TradeDetail sepQuery = new TradeDetail();
		sepQuery.setBillId(billId);
		sepQuery.setTradeType(TradeTypeEnum.NONE.getCode());
		TradeDetail tradeDetail = this.tradeDetailService.listByExample(sepQuery).stream().findFirst().orElse(null);
		assertNull(tradeDetail);

		// ------doCheckIn--------
		Pair<CheckinOutRecord, TradeDetail> pair = super.doCheckIn(billId, CheckinStatusEnum.ALLOWED);
		CheckinOutRecord inRecord = pair.getLeft();
		TradeDetail tradeDetailItem = pair.getRight();

		assertEquals(tradeDetailItem.getSaleStatus(), SaleStatusEnum.FOR_SALE.getCode());
		assertEquals(tradeDetailItem.getCheckinRecordId(), inRecord.getId());
		assertEquals(inRecord.getTradeDetailId(), tradeDetailItem.getId());

		// ==========doCheckout==========
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
