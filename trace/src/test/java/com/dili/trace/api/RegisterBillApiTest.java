package com.dili.trace.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStateEnum;
import com.dili.trace.service.RegisterBillService;

public class RegisterBillApiTest extends AutoWiredBaseTest {
	@Autowired
	RegisterBillApi registerBillApi;
	@Autowired
	RegisterBillService registerBillService;

	@MockBean
	LoginSessionContext sessionContext;
//	private MockMvc mockMvc;
//	@Injectable

	@BeforeEach
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testcreateList() {
		
		CreateListBillParam createListBillParam=new CreateListBillParam();
		this.registerBillApi.createList(createListBillParam);
	}

	@Test
	public void doVerify() {
		Mockito.doReturn(new OperatorUser(0L, "test")).when(this.sessionContext).getLoginUserOrException(Mockito.any());
		RegisterBill query = new RegisterBill();
		query.setVerifyState(BillVerifyStateEnum.NONE.getCode());
		RegisterBill input = registerBillService.listByExample(query).stream().findFirst().orElse(null);
		input.setVerifyState(BillVerifyStateEnum.PASSED.getCode());
		BaseOutput<Long> out = this.registerBillApi.doVerify(input);
		System.out.println(out);
	}
}
