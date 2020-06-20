package com.dili.trace.api;

import org.assertj.core.util.Arrays;
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
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.enums.BillVerifyStateEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;
import com.google.common.collect.Lists;

public class CheckinoutApiTest extends AutoWiredBaseTest {
	@Autowired
	CheckinOutRecordApi checkinoutApi;
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
	public void doCheckin() {

		RegisterBill query = new RegisterBill();
		query.setVerifyState(BillVerifyStateEnum.PASSED.getCode());
		RegisterBill item = registerBillService.listByExample(query).stream().findFirst().orElse(null);
		CheckInApiInput input = new CheckInApiInput();
		input.setBillIdList(Lists.newArrayList(item.getId()));
		input.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
		BaseOutput<Long> out = this.checkinoutApi.doCheckin(input);
		System.out.println(out.isSuccess());
	}

}
