package com.dili.trace.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.CheckInApiInput;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.input.RegisterBillApiInputDto;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.enums.*;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.StreamEx;

import static org.junit.jupiter.api.Assertions.*;

@EnableDiscoveryClient
public class RegisterBillServiceTest extends AutoWiredBaseTest {
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    CheckinOutRecordService checkinOutRecordService;
    @Autowired
    TradeDetailService tradeDetailService;

    @SpyBean
    CustomerRpcService clientRpcService;


    @Test
    public void createRegisterBillList() {
        CreateRegisterBillInputDto inputDto = new CreateRegisterBillInputDto();
        inputDto.setRegistType(RegistTypeEnum.NONE.getCode());
        inputDto.setPlate("川A12345");
        inputDto.setBrandName("好巴适");
        inputDto.setArrivalTallyno("123");
        inputDto.setArrivalDatetime(LocalDateTime.now());

        Long marketId = 8L;
        List<CreateRegisterBillInputDto> inputBillDtoList = Lists.newArrayList(inputDto);

        Long customerId = 1L;
        CustomerExtendDto customerExtendDto = new CustomerExtendDto();
        customerExtendDto.setId(customerId);
        customerExtendDto.setName("testuser");
        Mockito.doReturn(customerExtendDto).when(this.clientRpcService)
                .findApprovedCustomerByIdOrEx(Mockito.anyLong(), Mockito.anyLong());

        Optional<OperatorUser> operatorUser = Optional.empty();
        CreatorRoleEnum creatorRoleEnum = CreatorRoleEnum.MANAGER;

        try {
            this.registerBillService.createRegisterBillList(marketId, inputBillDtoList, customerId, operatorUser, creatorRoleEnum);
        } catch (TraceBizException e) {
            assertEquals(e.getMessage(),"商品重量不能为空");
        }
        try {
            inputDto.setWeight(BigDecimal.valueOf(-1));
            this.registerBillService.createRegisterBillList(marketId, inputBillDtoList, customerId, operatorUser, creatorRoleEnum);
        } catch (TraceBizException e) {
            assertEquals(e.getMessage(),"商品重量不能小于0");
        }

        try {
            inputDto.setWeight(BigDecimal.valueOf(99999999L+1));
            this.registerBillService.createRegisterBillList(marketId, inputBillDtoList, customerId, operatorUser, creatorRoleEnum);
        } catch (TraceBizException e) {
            assertEquals(e.getMessage(),"商品重量不能大于99999999");
        }


        inputDto.setWeight(BigDecimal.TEN);
        try {
            this.registerBillService.createRegisterBillList(marketId, inputBillDtoList, customerId, operatorUser, creatorRoleEnum);
        } catch (TraceBizException e) {
            assertEquals(e.getMessage(),"商品产地不能为空");
        }

        inputDto.setProductId(1L);
        inputDto.setProductName("白菜");

        try {
            this.registerBillService.createRegisterBillList(marketId, inputBillDtoList, customerId, operatorUser, creatorRoleEnum);
        } catch (TraceBizException e) {
            assertEquals(e.getMessage(),"商品产地不能为空");
        }

        inputDto.setOriginId(2L);
        inputDto.setOriginName("四川成都");
        List<Long> idList = this.registerBillService.createRegisterBillList(marketId, inputBillDtoList, customerId, operatorUser, creatorRoleEnum);
        assertNotNull(idList);
        System.out.println(idList);
    }

    @Test
    public void getBillDetail() {
        RegisterBill registerBill = registerBillService.get(5L);
        System.out.println(JSON.toJSONString(registerBill));
    }

    @Test
    public void doVerifyBeforeCheckIn() {
        RegisterBill query = new RegisterBill();
        query.setVerifyStatus(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
        RegisterBill input = StreamEx.of(this.registerBillService.listByExample(query)).findFirst().orElse(null);
        assertNotNull(input);
        OperatorUser operatorUser = new OperatorUser(1L, "test");
        input.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
        Long billId = this.registerBillService.doVerifyBeforeCheckIn(input, Optional.ofNullable(operatorUser));
        assertNotNull(billId);
    }

    @Test
    public void doVerifyAfterCheckIn() {

        RegisterBill query = new RegisterBill();
        query.setVerifyStatus(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
        RegisterBill input = StreamEx.of(this.registerBillService.listByExample(query)).findFirst().orElse(null);
        assertNotNull(input);
        assertTrue(BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(input.getVerifyStatus()));
        CheckInApiInput checkInApiInput = new CheckInApiInput();
        checkInApiInput.setBillIdList(Lists.newArrayList(input.getId()));
        checkInApiInput.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
        List<CheckinOutRecord> checkInList = this.checkinOutRecordService.doCheckin(
                Optional.ofNullable(new OperatorUser(1L, "test")), checkInApiInput.getBillIdList(),
                CheckinStatusEnum.ALLOWED);
        assertTrue(checkInList.size() == 1);
        CheckinOutRecord recordItem = checkInList.get(0);
        assertTrue(CheckinOutTypeEnum.IN.equalsToCode(recordItem.getInout()));
        assertTrue(CheckinStatusEnum.ALLOWED.equalsToCode(recordItem.getStatus()));

        OperatorUser operatorUser = new OperatorUser(1L, "test");
        input.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
        Long billId = this.registerBillService.doVerifyAfterCheckIn(input.getId(), input.getVerifyStatus(),
                input.getReason(), Optional.ofNullable(operatorUser));
        assertNotNull(billId);

    }

    @Test
    @Transactional
    public void selectByIdForUpdate() {
        this.registerBillService.selectByIdForUpdate(5L).ifPresent(bill -> {
            System.out.println(bill);
        });
    }

    @Test
    public void viewTradeDetailBill() {
        RegisterBillOutputDto dto = this.registerBillService.viewTradeDetailBill(new RegisterBillApiInputDto());
        System.out.println(dto);
    }

    @Test
    public void listPageCheckInData() {
        RegisterBillDto query = new RegisterBillDto();
//        query.setCreatedStart("2020-07-15 00:00:00");
//        query.setCreatedEnd("2020-07-21 23:59:59");
        query.setUserId(458L);
        Map<Integer, Map<String, List<RegisterBill>>> mapdata = this.registerBillService.listPageCheckInData(query);
        System.out.println(mapdata);
    }
}