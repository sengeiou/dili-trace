package com.dili.trace.api.client;

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
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;

public class ClientRegisterBillApiTest extends AutoWiredBaseTest {
	@Autowired
	ClientRegisterBillApi clientRegisterBillApi;
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
		rb.setWeightUnit(WeightUnitEnum.KILO.getCode());
		rb.setProductId(item.getProductId());
		rb.setProductName(item.getProductName());
		rb.setOriginId(item.getOriginId());
		rb.setOriginName(item.getOriginName());
		BaseOutput out = this.clientRegisterBillApi.createList(createListBillParam);
		System.out.println(out.isSuccess());
	}

}
