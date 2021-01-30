package com.dili.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.input.CheckInApiInput;
import com.dili.trace.api.input.CheckOutApiInput;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinOutTypeEnum;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.CheckoutStatusEnum;
import com.dili.trace.enums.ImageCertTypeEnum;
import com.dili.trace.enums.PreserveTypeEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.enums.VerifyTypeEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.TradeRequestService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import one.util.streamex.StreamEx;

public class AutoWiredBaseTest extends BaseTestWithouMVC {
	@Autowired
	UpStreamService UpStreamService;
	@Autowired
	protected RegisterBillService registerBillService;
	@Autowired
	CheckinOutRecordService checkinOutRecordService;
	@Autowired
	UserService userService;
	@Autowired
	AssetsRpcService categoryService;
	@Autowired
	TradeDetailService tradeDetailService;
	@Autowired
    TradeRequestService tradeRequestService;

	@BeforeEach
	public void mockInit() {

		/*
		 * Mockito.doNothing().when(this.customerOrderProcessor).createProcess(Mockito.
		 * any());
		 * Mockito.doNothing().when(this.customerOrderProcessor).cancelCarrierAssign(
		 * Mockito.any());
		 * 
		 * Mockito.doReturn(BaseOutput.success().setData(Arrays.asList(this.
		 * mockedProduct))).when(this.productRpc) .listByIds(Mockito.any());
		 */

	}


//	@Test
//	public void test() {
//		this.UpStreamService.listPageUpStream(11L, new UpStream());
//	}
/*
	protected Category findCategory() {
		//TODO
		Category query = new Category();

	*//*	query.setPage(1);
		query.setRows(1);
		Category categoryItem = StreamEx.of(this.categoryService.listPageByExample(query).getDatas()).findFirst()
				.orElse(null);
		assertNotNull(categoryItem);
		return categoryItem;*//*
		return null;
	}*/

	protected UserInfo findUser() {
		UserInfo query = new UserInfo();
		query.setPage(1);
		query.setRows(1);
		UserInfo userItem = StreamEx.of(this.userService.listPageByExample(query).getDatas()).findFirst().orElse(null);
		assertNotNull(userItem);
		return userItem;
	}
	protected List<UserInfo> findUsers() {
		UserInfo query = new UserInfo();
		query.setPage(1);
		query.setRows(10);
		return this.userService.listPageByExample(query).getDatas();
	}

	protected RegisterBill buildBill() {
		UserInfo userItem = this.findUser();
//		Category categoryItem = this.findCategory();
		RegisterBill bill = new RegisterBill();
//		bill.setBillType(BillTypeEnum.NONE.getCode());
		bill.setVerifyType(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
		bill.setPreserveType(PreserveTypeEnum.FRESH.getCode());
		bill.setWeight(BigDecimal.valueOf(100L));
		bill.setWeightUnit(WeightUnitEnum.JIN.getCode());
		bill.setTruckType(TruckTypeEnum.FULL.getCode());
		bill.setUserId(userItem.getId());
		bill.setName(userItem.getName());
		bill.setIdCardNo(userItem.getCardNo());
		bill.setAddr(userItem.getAddr());
//
//		bill.setProductId(categoryItem.getId());
//		bill.setProductName(categoryItem.getName());
		bill.setOriginId(1L);
		bill.setOriginName("国外");
		return bill;
	}

	protected RegisterBill createRegisterBill(RegisterBill bill) {
		assertNotNull(bill);
		ImageCert imageCert = new ImageCert();
		imageCert.setUid("imageurl");
		imageCert.setCertType(ImageCertTypeEnum.DETECT_REPORT.getCode());
		List<ImageCert> imageCertList = Lists.newArrayList(imageCert);
		bill.setImageCertList(imageCertList);
		Long billId = this.registerBillService.createRegisterBill(bill, Optional.ofNullable(new OperatorUser(1L, "test")));
		assertNotNull(billId);
		RegisterBill billItem = this.registerBillService.get(billId);
		assertNotNull(billItem);
		assertTrue(BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(billItem.getVerifyStatus()));
		return billItem;
	}

	protected Pair<CheckinOutRecord,TradeDetail> doCheckIn(Long billId, CheckinStatusEnum checkinStatusEnum) {
		CheckInApiInput input = new CheckInApiInput();
		input.setBillIdList(Lists.newArrayList(billId));
		input.setCheckinStatus(checkinStatusEnum.getCode());
		List<CheckinOutRecord> list = this.checkinOutRecordService.doCheckin(Optional.ofNullable(new OperatorUser(1L, "test")), input.getBillIdList(),CheckinStatusEnum.ALLOWED);
		assertNotNull(list);
		assertTrue(list.size() == 1);
		CheckinOutRecord record=list.get(0);
		assertNotNull(record);
		assertTrue(CheckinOutTypeEnum.IN.equalsToCode(record.getInout()));
		assertTrue(checkinStatusEnum.equalsToCode(record.getStatus()));

		TradeDetail sepQuery = new TradeDetail();
		sepQuery.setBillId(billId);
		sepQuery.setTradeType(TradeTypeEnum.NONE.getCode());
		TradeDetail tradeDetailItem = this.tradeDetailService.listByExample(sepQuery).stream().findFirst().orElse(null);
		assertNotNull(tradeDetailItem);
		assertEquals(tradeDetailItem.getCheckinStatus(), checkinStatusEnum.getCode());
		assertEquals(tradeDetailItem.getCheckoutStatus(), CheckoutStatusEnum.NONE.getCode());
		assertEquals(tradeDetailItem.getTradeType(), TradeTypeEnum.NONE.getCode());
		return MutablePair.of(record, tradeDetailItem);

	}

	protected Long doVerifyBeforeCheckIn(Long billId, BillVerifyStatusEnum verifyStatusEnum) {
		RegisterBill bill = new RegisterBill();
		bill.setId(billId);
		bill.setVerifyStatus(verifyStatusEnum.getCode());
		this.registerBillService.doVerifyBeforeCheckIn(bill, Optional.ofNullable(new OperatorUser(1L, "test")));

		RegisterBill billItem = this.registerBillService.get(billId);
		assertNotNull(billItem);
		assertTrue(verifyStatusEnum.equalsToCode(billItem.getVerifyStatus()));
		assertTrue(VerifyTypeEnum.PASSED_BEFORE_CHECKIN.equalsToCode(billItem.getVerifyType()));

		return billId;
	}

	protected Long doVerifyAfterCheckIn(Long billId, BillVerifyStatusEnum verifyStatusEnum) {
		RegisterBill bill = new RegisterBill();
		bill.setId(billId);
		bill.setVerifyStatus(verifyStatusEnum.getCode());
		this.registerBillService.doVerifyAfterCheckIn(bill.getId(),bill.getVerifyStatus(),bill.getReason(), Optional.ofNullable(new OperatorUser(1L, "test")));
		RegisterBill billItem = this.registerBillService.get(billId);
		assertNotNull(billItem);
		assertTrue(verifyStatusEnum.equalsToCode(billItem.getVerifyStatus()));
		assertTrue(VerifyTypeEnum.PASSED_AFTER_CHECKIN.equalsToCode(billItem.getVerifyType()));

		return billId;
	}
	protected CheckinOutRecord doCheckOut(Long tradeDetailId, CheckoutStatusEnum checkoutStatusEnum) {
		assertNotNull(tradeDetailId);
		TradeDetail tradeDetailItem=this.tradeDetailService.get(tradeDetailId);
		assertNotNull(tradeDetailItem);
		CheckOutApiInput outapiinput=new CheckOutApiInput();
		outapiinput.setCheckoutStatus(checkoutStatusEnum.getCode());
		outapiinput.setTradeDetailIdList(Lists.newArrayList(tradeDetailId));

		List<CheckinOutRecord>checkoutList=this.checkinOutRecordService.doCheckout(new OperatorUser(1L, "test"), outapiinput);
		assertNotNull(checkoutList);
		assertTrue(checkoutList.size()==1);
		CheckinOutRecord checkinOutRecord=checkoutList.get(0);

		return checkinOutRecord;
	}
}
