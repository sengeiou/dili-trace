package com.dili.trace.api.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.manager.ManagerCheckinOutRecordApi;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestInstance(Lifecycle.PER_CLASS)
public class ManagerCheckinOutRecordApiTest extends AutoWiredBaseTest {
    @Autowired
    ManagerCheckinOutRecordApi managerCheckinOutRecordApi;
  
    @MockBean
	LoginSessionContext sessionContext;
	// private MockMvc mockMvc;
	// @Injectable
	private User userItem;

	@BeforeEach
	public void before() {
		MockitoAnnotations.initMocks(this);
		userItem = super.findUser();
		assertNotNull(userItem);
		Mockito.doReturn(userItem.getId()).when(sessionContext).getAccountId();
		Mockito.doReturn(userItem.getName()).when(sessionContext).getUserName();
		Mockito.doReturn(new OperatorUser(userItem.getId(), userItem.getName())).when(this.sessionContext)
				.getLoginUserOrException(Mockito.any());
	}
    @Test
    public void listPageCheckInData() {
        RegisterBillDto inputDto = new RegisterBillDto();
        inputDto.setUserId(2L);
        BaseOutput<Map<Integer, Object>>out=this.managerCheckinOutRecordApi.listPageCheckInData(inputDto);
        assertNotNull(out);
        assertTrue(out.isSuccess());
        Map<Integer, Object> map = out.getData();
        assertNotNull(map);
        System.out.println(map);
        assertTrue(map.size()>0);
    }

}