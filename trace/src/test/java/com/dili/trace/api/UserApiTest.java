package com.dili.trace.api;

import java.util.Date;
import java.util.Map;

import org.apache.poi.sl.usermodel.ObjectMetaData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.User;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Rollback(false)
public class UserApiTest extends AutoWiredBaseTest {

	@Autowired
	UserApi userApi;


	@Test
	public void register() {
		JSONObject object = new JSONObject();
		object.put("phone", "15928695074");
		object.put("name", "庞先生");
		object.put("cardNo", "123");
		object.put("password", "123123");
		object.put("checkCode", "456634");
		object.put("addr", "地址。。。");
		User user = JSONObject.parseObject(object.toJSONString(),User.class);
		
		userApi.register(user);
	}
	
	@Test
	public void sendVerificationCode() {
		JSONObject object = new JSONObject();
		object.put("phone", "15928695074");
		userApi.sendVerificationCode(object);
	}


}
