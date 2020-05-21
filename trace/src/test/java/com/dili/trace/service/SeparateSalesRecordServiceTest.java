package com.dili.trace.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.dto.SeparateSalesApiListOutput;
import com.dili.trace.api.dto.SeparateSalesApiListQueryInput;

public class SeparateSalesRecordServiceTest extends AutoWiredBaseTest {
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;

	@Test
	public void dd() {
		SeparateSalesApiListQueryInput queryInput=new SeparateSalesApiListQueryInput();
		queryInput.setId(189L);
		SeparateSalesApiListOutput output=this.separateSalesRecordService.listByQueryInput(queryInput).stream().findFirst().orElse(null);
		System.out.println(output);
	}

}
