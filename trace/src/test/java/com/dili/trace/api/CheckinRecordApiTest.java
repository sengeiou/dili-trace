package com.dili.trace.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.BaseTestWithouMVC;
import com.dili.trace.api.dto.CheckInApiDetailOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.RegisterBillDto;

public class CheckinRecordApiTest extends BaseTestWithouMVC {
	@Autowired
	CheckinRecordApi checkinRecordApi;

	@Test
	public void listPagedAvailableCheckInData() {
		RegisterBillDto query = DTOUtils.newDTO(RegisterBillDto.class);
		query.setUserId(26L);
		BaseOutput<BasePage<RegisterBill>> page = this.checkinRecordApi.listPagedAvailableCheckInData(query);
		System.out.println(page);
	}

	@Test
	public void getCheckInDetail() {
		RegisterBillDto query = DTOUtils.newDTO(RegisterBillDto.class);
		query.setId(41264L);
		BaseOutput<CheckInApiDetailOutput> output = this.checkinRecordApi.getCheckInDetail(query);
		System.out.println(output);
	}

}
