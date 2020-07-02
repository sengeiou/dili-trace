package com.dili.trace.api.manager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.VerifyTypeEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ManagerRegisterBillApiTest extends AutoWiredBaseTest {
    @Autowired
    ManagerRegisterBillApi managerRegisterBillApi;

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
    public void countByVerifyStatus() {
        RegisterBill input = new RegisterBill();
        input.setVerifyType(VerifyTypeEnum.VERIFY_AFTER_CHECKIN.getCode());
        BaseOutput<List<VerifyStatusCountOutputDto>> out=this.managerRegisterBillApi.countByVerifyStatus(input);
        assertNotNull(out);

    }

}