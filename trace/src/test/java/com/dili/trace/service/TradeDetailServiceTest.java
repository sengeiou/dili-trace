package com.dili.trace.service;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.dto.TradeDetailInputDto;
import com.dili.trace.dto.TradeDetailInputWrapperDto;
@TestInstance(Lifecycle.PER_CLASS)
public class TradeDetailServiceTest extends AutoWiredBaseTest {
	@Autowired
	TradeDetailService tradeDetailService;

	@Test
	public void doReturning() {
		TradeDetailInputWrapperDto input = new TradeDetailInputWrapperDto();
		input.setTradeDetailInputList(new ArrayList<TradeDetailInputDto>());
		
		TradeDetailInputDto dto=new TradeDetailInputDto();
		dto.setTradeDetailId(1L);
		
		input.getTradeDetailInputList().add(dto);
		Long userId = 1L;
		this.tradeDetailService.doReturning(input, userId);
	}

}
