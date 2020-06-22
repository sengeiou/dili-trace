package com.dili.trace.api;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.User;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;

public class CheckinoutApiTest extends AutoWiredBaseTest {

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


}
