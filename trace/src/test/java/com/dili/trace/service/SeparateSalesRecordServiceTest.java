package com.dili.trace.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.output.SeparateSalesApiListOutput;
import com.dili.trace.api.output.SeparateSalesApiListQueryInput;

public class SeparateSalesRecordServiceTest extends AutoWiredBaseTest {
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;

	@Test
	public void listByQueryInput() throws Exception {
		SeparateSalesApiListQueryInput queryInput=new SeparateSalesApiListQueryInput();
		queryInput.setUserId(3L);
		queryInput.setId(2L);
		queryInput.setCreatedStart("2020-12-12 12:12:12");
		queryInput.setCreatedEnd("2020-12-12 23:23:23");
		queryInput.setLikeProductName("abc");
		this.separateSalesRecordService.listPageByQueryInput(queryInput);
		SeparateSalesApiListOutput output=this.separateSalesRecordService.listByQueryInput(queryInput).stream().findFirst().orElse(null);
		System.out.println(output);
	}

}
