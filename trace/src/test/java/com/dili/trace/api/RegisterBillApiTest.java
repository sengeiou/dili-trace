package com.dili.trace.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.client.ClientRegisterBillApi;
import com.dili.trace.api.dto.CreateRegisterBillInputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;

public class RegisterBillApiTest extends AutoWiredBaseTest {
	@Autowired
	ClientRegisterBillApi registerBillApi;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	UserService userService;

	@MockBean
	LoginSessionContext sessionContext;
//	private MockMvc mockMvc;
//	@Injectable

	@BeforeEach
	public void before() {
		MockitoAnnotations.initMocks(this);
		User user = this.userService.listPageByExample(DTOUtils.newDTO(User.class)).getDatas().stream().findFirst()
				.orElse(null);
		Mockito.doReturn(user.getId()).when(sessionContext).getAccountId();
	}

	@Test
	public void testcreateList() {

		RegisterBill query = new RegisterBill();
		query.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
		RegisterBill item = registerBillService.listPageByExample(query).getDatas().stream().findFirst().orElse(null);

		CreateListBillParam createListBillParam = new CreateListBillParam();
		List<CreateRegisterBillInputDto> registerBills = new ArrayList<CreateRegisterBillInputDto>();
		createListBillParam.setRegisterBills(registerBills);

		CreateRegisterBillInputDto rb = new CreateRegisterBillInputDto();
		registerBills.add(rb);
		rb.setWeight(BigDecimal.TEN);
		rb.setProductId(item.getProductId());
		rb.setProductName(item.getProductName());
		rb.setOriginId(item.getOriginId());
		rb.setOriginName(item.getOriginName());
		BaseOutput out = this.registerBillApi.createList(createListBillParam);
		System.out.println(out.isSuccess());
	}

	@Test
	public void doVerify() {
		Mockito.doReturn(new OperatorUser(0L, "test")).when(this.sessionContext).getLoginUserOrException(Mockito.any());
		RegisterBill query = new RegisterBill();
		query.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
		RegisterBill input = registerBillService.listByExample(query).stream().findFirst().orElse(null);
		input.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
//		BaseOutput<Long> out = this.registerBillApi.doVerify(input);
//		System.out.println(out);
	}
}
