package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import com.dili.ss.domain.BasePage;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.output.TradeDetailBillOutput;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.TradeDetailInputDto;
import com.dili.trace.dto.TradeDetailInputWrapperDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hutool.json.JSONObject;
import one.util.streamex.StreamEx;

@TestInstance(Lifecycle.PER_CLASS)
public class TradeDetailServiceTest extends AutoWiredBaseTest {
	@Autowired
	TradeDetailService tradeDetailService;

	// @Test
	// public void doReturning() {
	// 	TradeDetailInputWrapperDto input = new TradeDetailInputWrapperDto();
	// 	input.setTradeDetailInputList(new ArrayList<TradeDetailInputDto>());

	// 	TradeDetailInputDto dto = new TradeDetailInputDto();
	// 	dto.setTradeDetailId(1L);

	// 	input.getTradeDetailInputList().add(dto);
	// 	Long userId = 1L;
	// 	this.tradeDetailService.doReturning(input, userId);
	// }

	@Test
	public void selectTradeDetailAndBill() {
		RegisterBillDto dto = new RegisterBillDto();
		dto.setUserId(super.findUser().getId());
		dto.setCreatedStart("2015-01-01 00:00:00");
		dto.setCreatedEnd("2020-12-31 23:59:59");
		dto.setLikeProductName("黄瓜");
		BasePage<TradeDetailBillOutput> page = this.tradeDetailService.selectTradeDetailAndBill(dto);
		assertNotNull(page);
		TradeDetailBillOutput out = StreamEx.of(page.getDatas()).findFirst().orElse(null);
		assertNotNull(out);
		assertNotNull(out.getTradeType());
		System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(out));
	}
}
