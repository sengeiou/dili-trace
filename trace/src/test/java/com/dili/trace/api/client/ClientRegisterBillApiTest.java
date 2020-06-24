package com.dili.trace.api.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.ImageCertTypeEnum;
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
		assertNotNull(user);
		Mockito.doReturn(user.getId()).when(sessionContext).getAccountId();
		Mockito.doReturn(user.getName()).when(sessionContext).getUserName();
		Mockito.doReturn(new OperatorUser(user.getId(), user.getName())).when(this.sessionContext)
				.getLoginUserOrException(Mockito.any());
	}

	@Test
	public void createRegisterBillList() {

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
		rb.setImageCertList(new ArrayList<ImageCert>());
		ImageCert imageCert = new ImageCert();
		imageCert.setUrl("imageurl");
		imageCert.setCertType(ImageCertTypeEnum.DETECT_REPORT.getCode());
		rb.getImageCertList().add(imageCert);
		BaseOutput out = this.clientRegisterBillApi.createRegisterBillList(createListBillParam);
		System.out.println(out.isSuccess());
	}

	@Test
	public void listPage() {
		BaseOutput<BasePage<RegisterBill>> output = this.clientRegisterBillApi.listPage(new RegisterBillDto());
		System.out.println(output);
	}

}
