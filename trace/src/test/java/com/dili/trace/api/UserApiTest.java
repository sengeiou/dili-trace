package com.dili.trace.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.manager.ManagerUserApi;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.enums.VocationTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.User;
import org.springframework.test.annotation.Rollback;

import java.util.Map;

@Rollback(false)
public class UserApiTest extends AutoWiredBaseTest {

	@Autowired
	UserApi userApi;
	@Autowired
	ManagerUserApi managerUserApi;


	@Test
	public void register() {
		JSONObject object = new JSONObject();
		object.put("phone", "15928695074");
		object.put("name", "庞先生");
		object.put("cardNo", "");
		object.put("password", "123123");
		object.put("checkCode", "456634");
		object.put("addr", "");
		User user = JSONObject.parseObject(object.toJSONString(),User.class);
		
		userApi.register(user);
	}
	
	@Test
	public void sendVerificationCode() {
		JSONObject object = new JSONObject();
		object.put("phone", "15928695074");
		userApi.sendVerificationCode(object);
	}

	@Test
	public void realNameCertificationReq(){
		JSONObject object = new JSONObject();
		object.put("id", "18");
		object.put("phone", "15928695074");
		object.put("name", "庞记水产公司");
		object.put("legal_person", "庞先生");
		object.put("cardNo", "110101199003070839");
		object.put("card_no_front_url", "/image/DETECT_REPORT/202005/9b7c78f4979049ab9c634b1fea9dcbd0.jpg");
		object.put("card_no_back_url", "/image/DETECT_REPORT/202005/9b7c78f4979049ab9c634b1fea9dcbd0.jpg");
		object.put("license", "45663LL89788W984");
		object.put("market_id", "1");
		object.put("user_type", UserTypeEnum.CORPORATE.getCode());
		object.put("vocationType", VocationTypeEnum.WHOLESALE.getCode());
		object.put("tally_area_nos", "101,102");
		object.put("manufacturing_license_url", "生产许可证");
		object.put("business_license_url", "营业执照");
		object.put("operation_license_url", "经营许可证");
		object.put("addr", "四川成都青羊区人民路9号");
		User user = JSONObject.parseObject(object.toJSONString(),User.class);

		userApi.realNameCertificationReq(user);
	}

	@Test
	public void certCount(){
		BaseOutput<Map<String,String>> out = managerUserApi.countGroupByValidateState(null);
		System.out.println("--------->" + out.getCode());
		System.out.println("--------->" + out.getData());
	}

	@Test
	public void pageUser(){
		JSONObject object = new JSONObject();
		object.put("validateState", "10");
//		object.put("page", "2");
		object.put("keyword", "5");
		UserInput user = JSONObject.parseObject(object.toJSONString(),UserInput.class);
		BaseOutput<BasePage<UserOutput>> out = managerUserApi.listUserCertByQuery(user);
		System.out.println(out.getCode());
		System.out.println(out.getData());
	}

	@Test
	public void userCertDetail(){
		JSONObject object = new JSONObject();
		object.put("id", "10");
		UserInput user = JSONObject.parseObject(object.toJSONString(),UserInput.class);
		BaseOutput<User> out = managerUserApi.userCertDetail(user);
		System.out.println(out.getCode());
	}

	@Test
	public void verifyUserCert(){
		JSONObject object = new JSONObject();
		object.put("id", "18");
		object.put("validateState", "30");
		UserInput user = JSONObject.parseObject(object.toJSONString(),UserInput.class);
		BaseOutput out = managerUserApi.verifyUserCert(user);
		System.out.println(out.getCode());
	}

}