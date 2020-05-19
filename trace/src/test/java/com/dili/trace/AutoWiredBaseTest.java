package com.dili.trace;

import com.dili.trace.domain.UpStream;
import com.dili.trace.service.UpStreamService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoWiredBaseTest extends BaseTestWithouMVC {
	@Autowired
	UpStreamService UpStreamService;
	@BeforeEach
	public void mockInit() {


	/*	Mockito.doNothing().when(this.customerOrderProcessor).createProcess(Mockito.any());
		Mockito.doNothing().when(this.customerOrderProcessor).cancelCarrierAssign(Mockito.any());

		Mockito.doReturn(BaseOutput.success().setData(Arrays.asList(this.mockedProduct))).when(this.productRpc)
				.listByIds(Mockito.any());
*/

	}
	@Test
	public void test(){
		this.UpStreamService.listPageUpStream(11L, new UpStream());
	}

}
